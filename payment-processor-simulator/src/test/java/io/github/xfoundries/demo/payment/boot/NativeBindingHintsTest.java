package io.github.xfoundries.demo.payment.boot;

import io.github.xfoundries.demo.contracts.EventEnvelope;
import io.github.xfoundries.demo.contracts.ExpenseClaimApprovedV1;
import io.github.xfoundries.demo.contracts.ReimbursementPaidV1;
import io.github.xfoundries.demo.contracts.ReimbursementPaymentFailedV1;
import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import static org.assertj.core.api.Assertions.assertThat;

class NativeBindingHintsTest {

    @Test
    void registersIntegrationContractsForNativeBinding() {
        RegisterReflectionForBinding binding = PaymentProcessorSimulatorApplication.class
                .getAnnotation(RegisterReflectionForBinding.class);

        assertThat(binding).isNotNull();
        assertThat(binding.value()).containsExactlyInAnyOrder(
                EventEnvelope.class,
                ExpenseClaimApprovedV1.class,
                ReimbursementPaidV1.class,
                ReimbursementPaymentFailedV1.class);
    }
}
