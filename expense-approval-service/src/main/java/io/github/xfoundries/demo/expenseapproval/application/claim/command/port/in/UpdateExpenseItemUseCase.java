package io.github.xfoundries.demo.expenseapproval.application.claim.command.port.in;

import io.github.xfoundries.demo.expenseapproval.application.claim.command.UpdateExpenseItemCommand;
import io.huahill.jfoundry.architecture.hexagonal.PrimaryPort;

@PrimaryPort
public interface UpdateExpenseItemUseCase {

    void updateItem(UpdateExpenseItemCommand command);
}
