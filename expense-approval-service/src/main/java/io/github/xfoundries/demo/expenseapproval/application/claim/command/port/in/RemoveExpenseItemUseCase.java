package io.github.xfoundries.demo.expenseapproval.application.claim.command.port.in;

import io.github.xfoundries.demo.expenseapproval.application.claim.command.RemoveExpenseItemCommand;
import io.huahill.jfoundry.architecture.hexagonal.PrimaryPort;

@PrimaryPort
public interface RemoveExpenseItemUseCase {

    void removeItem(RemoveExpenseItemCommand command);
}
