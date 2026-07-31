package io.github.xfoundries.demo.payment.boot;

import io.github.xfoundries.demo.contracts.EventEnvelope;
import io.github.xfoundries.demo.contracts.ExpenseClaimApprovedV1;
import io.github.xfoundries.demo.contracts.ReimbursementPaidV1;
import io.github.xfoundries.demo.contracts.ReimbursementPaymentFailedV1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@SpringBootApplication(scanBasePackages = "io.github.xfoundries.demo.payment")
@RegisterReflectionForBinding({
        EventEnvelope.class,
        ExpenseClaimApprovedV1.class,
        ReimbursementPaidV1.class,
        ReimbursementPaymentFailedV1.class
})
public class PaymentProcessorSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentProcessorSimulatorApplication.class, args);
    }
}
