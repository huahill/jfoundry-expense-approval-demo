package io.github.xfoundries.demo.expenseapproval.application.claim.command.handler;

import io.huahill.jfoundry.application.ApplicationService;
import io.github.xfoundries.demo.expenseapproval.application.claim.command.ApproveExpenseClaimByFinanceCommand;
import io.github.xfoundries.demo.expenseapproval.application.claim.command.port.in.ApproveExpenseClaimByFinanceUseCase;
import io.github.xfoundries.demo.expenseapproval.application.approval.FinalApprovalCoordinator;
import io.huahill.jfoundry.architecture.cqrs.CommandHandler;

@ApplicationService
public class ApproveExpenseClaimByFinanceCommandHandler implements ApproveExpenseClaimByFinanceUseCase {

    private final FinalApprovalCoordinator coordinator;

    public ApproveExpenseClaimByFinanceCommandHandler(FinalApprovalCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    @CommandHandler
    public void approveByFinance(ApproveExpenseClaimByFinanceCommand command) {
        coordinator.approveByFinance(command);
    }
}
