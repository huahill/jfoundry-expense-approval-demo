package io.github.xfoundries.demo.expenseapproval.boot;

import io.github.xfoundries.demo.contracts.EventEnvelope;
import io.github.xfoundries.demo.contracts.ExpenseClaimApprovedV1;
import io.github.xfoundries.demo.contracts.ReimbursementPaidV1;
import io.github.xfoundries.demo.contracts.ReimbursementPaymentFailedV1;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@EntityScan(basePackages = "io.github.xfoundries.demo.expenseapproval.adapter.out")
@SpringBootApplication(scanBasePackages = "io.github.xfoundries.demo.expenseapproval")
@RegisterReflectionForBinding({
        EventEnvelope.class,
        ExpenseClaimApprovedV1.class,
        ReimbursementPaidV1.class,
        ReimbursementPaymentFailedV1.class
})
public class ExpenseApprovalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseApprovalApplication.class, args);
    }
}
