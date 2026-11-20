package io.github.xfoundries.demo.expenseapproval.application.claim.command.handler;

import io.huahill.jfoundry.application.ApplicationService;

import java.time.Clock;

import io.github.xfoundries.demo.expenseapproval.application.identity.ApprovalRole;
import io.github.xfoundries.demo.expenseapproval.application.claim.command.CreateExpenseClaimCommand;
import io.github.xfoundries.demo.expenseapproval.application.claim.command.port.in.CreateExpenseClaimUseCase;
import io.github.xfoundries.demo.expenseapproval.domain.model.ExpenseClaim;
import io.github.xfoundries.demo.expenseapproval.domain.model.ExpenseClaimId;
import io.github.xfoundries.demo.expenseapproval.domain.repository.ExpenseClaimRepository;
import io.huahill.jfoundry.application.exception.ExternalAccessException;
import io.huahill.jfoundry.application.exception.InvalidArgumentException;
import io.huahill.jfoundry.application.transaction.TransactionRunner;
import io.huahill.jfoundry.architecture.cqrs.CommandHandler;

@ApplicationService
public class CreateExpenseClaimCommandHandler implements CreateExpenseClaimUseCase {

    private final ExpenseClaimRepository repository;
    private final Clock clock;
    private final TransactionRunner transactions;

    public CreateExpenseClaimCommandHandler(
            ExpenseClaimRepository repository, Clock clock, TransactionRunner transactions) {
        this.repository = repository;
        this.clock = clock;
        this.transactions = transactions;
    }

    @Override
    @CommandHandler
    public ExpenseClaimId create(CreateExpenseClaimCommand command) {
        try {
            return transactions.call(() -> {
                if (command.actor().role() != ApprovalRole.EMPLOYEE) {
                    throw new InvalidArgumentException("This action requires role EMPLOYEE");
                }
                ExpenseClaim claim = ExpenseClaim.draft(
                        ExpenseClaimId.generate(), command.actor().userId(), command.title(), clock.instant());
                repository.add(claim);
                return claim.id();
            });
        } catch (RuntimeException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ExternalAccessException("Expense claim transaction failed", exception);
        }
    }
}
