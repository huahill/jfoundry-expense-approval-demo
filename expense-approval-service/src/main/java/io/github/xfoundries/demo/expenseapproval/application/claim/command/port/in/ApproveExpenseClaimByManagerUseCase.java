package io.github.xfoundries.demo.expenseapproval.application.claim.command.port.in;

import io.github.xfoundries.demo.expenseapproval.application.claim.command.ApproveExpenseClaimByManagerCommand;
import io.huahill.jfoundry.architecture.hexagonal.PrimaryPort;

@PrimaryPort
public interface ApproveExpenseClaimByManagerUseCase {

    void approveByManager(ApproveExpenseClaimByManagerCommand command);
}
