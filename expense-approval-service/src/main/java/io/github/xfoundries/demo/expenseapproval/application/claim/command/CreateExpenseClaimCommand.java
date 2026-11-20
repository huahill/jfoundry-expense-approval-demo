package io.github.xfoundries.demo.expenseapproval.application.claim.command;

import io.github.xfoundries.demo.expenseapproval.application.identity.ApprovalActor;
import io.huahill.jfoundry.architecture.cqrs.Command;

@Command
public record CreateExpenseClaimCommand(ApprovalActor actor, String title) {
}
