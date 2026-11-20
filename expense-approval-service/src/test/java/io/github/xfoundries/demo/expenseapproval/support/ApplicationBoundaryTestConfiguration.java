package io.github.xfoundries.demo.expenseapproval.support;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ApplicationBoundaryTestConfiguration {

    @Bean
    TestApplicationBoundary testApplicationBoundary() {
        return new TestApplicationBoundary();
    }
}
