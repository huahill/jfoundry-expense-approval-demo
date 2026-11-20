package io.github.xfoundries.demo.expenseapproval.application.claim.command.handler;

import io.huahill.jfoundry.application.ApplicationService;
import io.github.xfoundries.demo.expenseapproval.application.identity.ApprovalRole;
import io.github.xfoundries.demo.expenseapproval.application.claim.command.RemoveExpenseItemCommand;
import io.github.xfoundries.demo.expenseapproval.application.claim.command.port.in.RemoveExpenseItemUseCase;
import io.github.xfoundries.demo.expenseapproval.domain.model.ExpenseClaim;
import io.huahill.jfoundry.architecture.cqrs.CommandHandler;

@ApplicationService
public class RemoveExpenseItemCommandHandler implements RemoveExpenseItemUseCase {

    private final ExpenseClaimCommandSupport support;

    public RemoveExpenseItemCommandHandler(ExpenseClaimCommandSupport support) {
        this.support = support;
    }

    @Override
    @CommandHandler
    public void removeItem(RemoveExpenseItemCommand command) {
        support.inTransaction(() -> {
            support.requireRole(command.actor(), ApprovalRole.EMPLOYEE);
            ExpenseClaim claim = support.load(command.claimId());
            claim.removeItem(command.actor().userId(), command.itemId(), support.now());
            support.save(claim);
            return null;
        });
    }
}
