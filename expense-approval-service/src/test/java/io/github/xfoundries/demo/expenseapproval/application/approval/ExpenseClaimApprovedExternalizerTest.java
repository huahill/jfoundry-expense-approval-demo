package io.github.xfoundries.demo.expenseapproval.application.approval;

import java.math.BigDecimal;
import java.time.Instant;

import io.github.xfoundries.demo.contracts.EventEnvelope;
import io.github.xfoundries.demo.contracts.ExpenseClaimApprovedV1;
import io.github.xfoundries.demo.expenseapproval.domain.model.ExpenseClaimApproved;
import io.github.xfoundries.demo.expenseapproval.domain.model.ExpenseClaimId;
import io.github.xfoundries.demo.expenseapproval.domain.model.Money;
import io.github.xfoundries.demo.expenseapproval.domain.model.UserId;
import org.jfoundry.application.event.externalization.ExternalizedEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExpenseClaimApprovedExternalizerTest {

    private static final Instant APPROVED_AT = Instant.parse("2026-07-31T16:30:00Z");

    private final ExpenseClaimApprovedExternalizer externalizer = new ExpenseClaimApprovedExternalizer();

    @Test
    void mapsTheDomainEventToTheVersionedApprovalIntegrationContract() {
        ExpenseClaimApproved domainEvent = new ExpenseClaimApproved(
                ExpenseClaimId.of("claim-1"),
                UserId.of("employee-1"),
                Money.positiveCny(new BigDecimal("123.45")),
                APPROVED_AT);

        ExternalizedEvent externalized = externalizer.externalize(domainEvent).getFirst();

        assertThat(externalized.topic()).isEqualTo("expense-approval.events.v1");
        assertThat(externalized.payloadType()).isEqualTo("expense-approval.claim-approved.v1");
        assertThat(externalized.payloadKey()).isEqualTo("claim-1");
        assertThat(externalized.aggregateType()).isEqualTo("ExpenseClaim");
        assertThat(externalized.aggregateId()).isEqualTo("claim-1");
        assertThat(externalized.aggregateVersion()).isNull();
        assertThat(externalized.payload()).isInstanceOfSatisfying(EventEnvelope.class, payload -> {
            @SuppressWarnings("unchecked")
            EventEnvelope<ExpenseClaimApprovedV1> envelope = (EventEnvelope<ExpenseClaimApprovedV1>) payload;
            assertThat(envelope.eventId()).isEqualTo(domainEvent.getEventId().toString());
            assertThat(envelope.eventType()).isEqualTo("ExpenseClaimApproved");
            assertThat(envelope.eventVersion()).isEqualTo(1);
            assertThat(envelope.occurredAt()).isEqualTo(APPROVED_AT);
            assertThat(envelope.aggregateId()).isEqualTo("claim-1");
            assertThat(envelope.payload()).isEqualTo(new ExpenseClaimApprovedV1(
                    "claim-1", "employee-1", new BigDecimal("123.45"), "CNY", APPROVED_AT));
        });
    }
}
