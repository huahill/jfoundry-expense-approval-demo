package io.github.xfoundries.demo.expenseapproval.application.claim.command.handler;

import io.huahill.jfoundry.application.ApplicationService;

import io.github.xfoundries.demo.expenseapproval.application.identity.ApprovalRole;
import io.github.xfoundries.demo.expenseapproval.application.claim.command.RejectExpenseClaimCommand;
import io.github.xfoundries.demo.expenseapproval.domain.model.ClaimState;
import io.github.xfoundries.demo.expenseapproval.domain.model.ExpenseClaim;
import io.github.xfoundries.demo.expenseapproval.domain.model.RejectionReason;
import io.huahill.jfoundry.application.exception.InvalidArgumentException;
import io.huahill.jfoundry.architecture.cqrs.CommandHandler;

@ApplicationService
public class RejectExpenseClaimCommandHandler {

    private final ExpenseClaimCommandSupport support;

    public RejectExpenseClaimCommandHandler(ExpenseClaimCommandSupport support) {
        this.support = support;
    }

    @CommandHandler
    public void reject(RejectExpenseClaimCommand command) {
        support.inTransaction(() -> {
            ExpenseClaim claim = support.load(command.claimId());
            ApprovalRole requiredRole = requiredRole(claim.state());
            support.requireRole(command.actor(), requiredRole);
            claim.reject(
                    command.actor().userId(), RejectionReason.of(command.reason()), support.now());
            support.save(claim);
            return null;
        });
    }

    private static ApprovalRole requiredRole(ClaimState state) {
        return switch (state) {
            case PENDING_MANAGER -> ApprovalRole.MANAGER;
            case PENDING_FINANCE -> ApprovalRole.FINANCE;
            default -> throw new InvalidArgumentException("Expense claim is not waiting for approval");
        };
    }
}
