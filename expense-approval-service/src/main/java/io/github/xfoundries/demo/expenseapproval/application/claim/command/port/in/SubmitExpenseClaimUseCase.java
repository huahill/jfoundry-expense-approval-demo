package io.github.xfoundries.demo.expenseapproval.application.claim.command.port.in;

import io.github.xfoundries.demo.expenseapproval.application.claim.command.SubmitExpenseClaimCommand;
import io.huahill.jfoundry.architecture.hexagonal.PrimaryPort;

@PrimaryPort
public interface SubmitExpenseClaimUseCase {

    void submit(SubmitExpenseClaimCommand command);
}
