package io.github.xfoundries.demo.expenseapproval.boot;

import io.github.xfoundries.demo.contracts.EventEnvelope;
import io.github.xfoundries.demo.contracts.ExpenseClaimApprovedV1;
import io.github.xfoundries.demo.contracts.ReimbursementPaidV1;
import io.github.xfoundries.demo.contracts.ReimbursementPaymentFailedV1;
import io.github.xfoundries.demo.expenseapproval.application.claim.query.ExpenseClaimQueryService;
import io.github.xfoundries.demo.expenseapproval.application.claim.query.ExpenseClaimViewReader;
import io.github.xfoundries.demo.expenseapproval.boot.config.ApplicationConfiguration;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class NativeBindingHintsTest {

    @Test
    void registersIntegrationContractsForNativeBinding() {
        RegisterReflectionForBinding binding = ExpenseApprovalApplication.class
                .getAnnotation(RegisterReflectionForBinding.class);

        assertThat(binding).isNotNull();
        assertThat(binding.value()).containsExactlyInAnyOrder(
                EventEnvelope.class,
                ExpenseClaimApprovedV1.class,
                ReimbursementPaidV1.class,
                ReimbursementPaymentFailedV1.class);
    }

    @Test
    void bindsMappersToTheSqlSessionFactoryForNativeAot() {
        MapperScan mapperScan = ExpenseApprovalApplication.class.getAnnotation(MapperScan.class);

        assertThat(mapperScan).isNotNull();
        assertThat(mapperScan.sqlSessionFactoryRef()).isEqualTo("sqlSessionFactory");
    }

    @Test
    void exposesTheApplicationServiceTypeToAot() throws NoSuchMethodException {
        Method factoryMethod = ApplicationConfiguration.class
                .getDeclaredMethod("expenseClaimQueries", ExpenseClaimViewReader.class);

        assertThat(factoryMethod.getReturnType()).isEqualTo(ExpenseClaimQueryService.class);
    }
}
