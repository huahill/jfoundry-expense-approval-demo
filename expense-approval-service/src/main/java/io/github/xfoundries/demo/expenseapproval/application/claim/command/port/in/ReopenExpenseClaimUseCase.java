package io.github.xfoundries.demo.expenseapproval.application.claim.command.port.in;

import io.github.xfoundries.demo.expenseapproval.application.claim.command.ReopenExpenseClaimCommand;
import io.huahill.jfoundry.architecture.hexagonal.PrimaryPort;

@PrimaryPort
public interface ReopenExpenseClaimUseCase {

    void reopen(ReopenExpenseClaimCommand command);
}
