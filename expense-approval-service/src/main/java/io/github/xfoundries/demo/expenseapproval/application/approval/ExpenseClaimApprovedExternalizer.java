package io.github.xfoundries.demo.expenseapproval.application.approval;

import java.util.List;

import io.github.xfoundries.demo.contracts.EventEnvelope;
import io.github.xfoundries.demo.contracts.ExpenseClaimApprovedV1;
import io.github.xfoundries.demo.expenseapproval.domain.model.ExpenseClaimApproved;
import org.jfoundry.application.event.externalization.DomainEventExternalizer;
import org.jfoundry.application.event.externalization.ExternalizedEvent;

public final class ExpenseClaimApprovedExternalizer implements DomainEventExternalizer<ExpenseClaimApproved> {

    private static final String TOPIC = "expense-approval.events.v1";
    private static final String PAYLOAD_TYPE = "expense-approval.claim-approved.v1";

    @Override
    public Class<ExpenseClaimApproved> sourceEventType() {
        return ExpenseClaimApproved.class;
    }

    @Override
    public List<ExternalizedEvent> externalize(ExpenseClaimApproved event) {
        String eventId = event.getEventId().toString();
        String claimId = event.claimId().value();
        EventEnvelope<ExpenseClaimApprovedV1> envelope = EventEnvelope.create(
                eventId,
                "ExpenseClaimApproved",
                1,
                event.approvedAt(),
                eventId,
                null,
                claimId,
                new ExpenseClaimApprovedV1(
                        claimId,
                        event.claimantId().value(),
                        event.amount().amount(),
                        "CNY",
                        event.approvedAt()));
        return List.of(new ExternalizedEvent(
                TOPIC,
                PAYLOAD_TYPE,
                envelope,
                claimId,
                "ExpenseClaim",
                claimId,
                null));
    }
}
