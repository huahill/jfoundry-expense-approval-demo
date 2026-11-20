package io.github.xfoundries.demo.expenseapproval.application.claim.command.handler;

import io.huahill.jfoundry.application.ApplicationService;
import io.github.xfoundries.demo.expenseapproval.application.identity.ApprovalRole;
import io.github.xfoundries.demo.expenseapproval.application.claim.command.ReopenExpenseClaimCommand;
import io.github.xfoundries.demo.expenseapproval.application.claim.command.port.in.ReopenExpenseClaimUseCase;
import io.github.xfoundries.demo.expenseapproval.domain.model.ExpenseClaim;
import io.huahill.jfoundry.architecture.cqrs.CommandHandler;

@ApplicationService
public class ReopenExpenseClaimCommandHandler implements ReopenExpenseClaimUseCase {

    private final ExpenseClaimCommandSupport support;

    public ReopenExpenseClaimCommandHandler(ExpenseClaimCommandSupport support) {
        this.support = support;
    }

    @Override
    @CommandHandler
    public void reopen(ReopenExpenseClaimCommand command) {
        support.inTransaction(() -> {
            support.requireRole(command.actor(), ApprovalRole.EMPLOYEE);
            ExpenseClaim claim = support.load(command.claimId());
            claim.reopen(command.actor().userId(), support.now());
            support.save(claim);
            return null;
        });
    }
}
