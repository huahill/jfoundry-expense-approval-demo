package io.github.xfoundries.demo.e2e;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.EnabledIf;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIf("nativeExecutablesPresent")
class NativeExpenseApprovalMessagingEndToEndTest
        extends AbstractExpenseApprovalMessagingEndToEndTest {

    private static final Duration STARTUP_TIMEOUT = Duration.ofSeconds(60);

    private final HttpClient readinessHttpClient = HttpClient.newHttpClient();

    private NativeApplicationProcess paymentApplication;
    private NativeApplicationProcess expenseApplication;


    static boolean nativeExecutablesPresent() {
        Path projectRoot = Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize().getParent();
        return projectRoot != null
                && Files.isExecutable(projectRoot.resolve(
                        "payment-processor-simulator/target/payment-processor-simulator"))
                && Files.isExecutable(projectRoot.resolve(
                        "expense-approval-service/target/expense-approval-service"));
    }

    @Override
    protected void startApplicationsForTest() throws Exception {
        Path projectRoot = Path.of(System.getProperty("user.dir"))
                .toAbsolutePath()
                .normalize()
                .getParent();
        if (projectRoot == null) {
            throw new IllegalStateException("Cannot locate the demo project root");
        }

        Path paymentExecutable = projectRoot.resolve(
                "payment-processor-simulator/target/payment-processor-simulator");
        Path expenseExecutable = projectRoot.resolve(
                "expense-approval-service/target/expense-approval-service");
        paymentApplication = startApplication(
                "payment-processor-simulator",
                paymentExecutable,
                paymentProperties());

        int expensePort = availablePort();
        Map<String, Object> expenseProperties = new java.util.LinkedHashMap<>(expenseProperties());
        expenseProperties.put("server.port", expensePort);
        expenseApplication = startApplication(
                "expense-approval-service",
                expenseExecutable,
                expenseProperties);

        objectMapper = JsonMapper.builder().build();
        expenseBaseUri = URI.create("http://localhost:" + expensePort);
        awaitExpenseHttpEndpoint();
        assertProcessIsAlive(paymentApplication);
    }

    @Override
    protected void stopApplicationsForTest() throws Exception {
        stopApplication(expenseApplication);
        stopApplication(paymentApplication);
    }

    private NativeApplicationProcess startApplication(
            String name, Path executable, Map<String, Object> properties) throws IOException {
        if (!Files.isExecutable(executable)) {
            throw new IllegalStateException("Native executable is missing or not executable: " + executable);
        }

        Path logFile = Files.createTempFile(name + "-native-e2e-", ".log");
        List<String> command = new ArrayList<>();
        command.add(executable.toString());
        properties.forEach((key, value) -> command.add("--" + key + '=' + value));
        Process process = new ProcessBuilder(command)
                .redirectErrorStream(true)
                .redirectOutput(logFile.toFile())
                .start();
        return new NativeApplicationProcess(name, process, logFile);
    }

    private void awaitExpenseHttpEndpoint() throws Exception {
        long deadline = System.nanoTime() + STARTUP_TIMEOUT.toNanos();
        Exception lastFailure = null;
        while (System.nanoTime() < deadline) {
            assertProcessIsAlive(expenseApplication);
            try {
                HttpRequest request = HttpRequest.newBuilder(expenseBaseUri.resolve("/api/claims/not-ready"))
                        .timeout(Duration.ofSeconds(2))
                        .header("X-User-Id", "native-readiness")
                        .header("X-User-Role", "EMPLOYEE")
                        .GET()
                        .build();
                readinessHttpClient.send(request, HttpResponse.BodyHandlers.discarding());
                return;
            } catch (IOException | InterruptedException exception) {
                if (exception instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                    throw exception;
                }
                lastFailure = exception;
                Thread.sleep(100);
            }
        }
        throw new AssertionError("Expense native application did not open its HTTP endpoint. "
                + processDiagnostics(), lastFailure);
    }

    private void assertProcessIsAlive(NativeApplicationProcess application) {
        if (!application.process().isAlive()) {
            throw new AssertionError(application.name() + " exited before the test could continue. "
                    + processDiagnostics());
        }
    }

    private String processDiagnostics() {
        return "Native process logs:\n"
                + applicationLog(expenseApplication)
                + applicationLog(paymentApplication);
    }

    private static String applicationLog(NativeApplicationProcess application) {
        if (application == null) {
            return "";
        }
        try {
            String content = Files.readString(application.logFile());
            int start = Math.max(0, content.length() - 8_000);
            return "[" + application.name() + "]\n" + content.substring(start) + '\n';
        } catch (IOException exception) {
            return "[" + application.name() + "] unable to read " + application.logFile()
                    + ": " + exception.getMessage() + '\n';
        }
    }

    private static void stopApplication(NativeApplicationProcess application) throws InterruptedException {
        if (application == null || !application.process().isAlive()) {
            return;
        }
        application.process().destroy();
        if (!application.process().waitFor(10, TimeUnit.SECONDS)) {
            application.process().destroyForcibly();
            application.process().waitFor(10, TimeUnit.SECONDS);
        }
    }

    private static int availablePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    private record NativeApplicationProcess(String name, Process process, Path logFile) {
    }
}
