package io.github.xfoundries.demo.expenseapproval.application.approval;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;

import io.github.xfoundries.demo.contracts.EventEnvelope;
import io.github.xfoundries.demo.contracts.ExpenseClaimApprovedV1;
import io.github.xfoundries.demo.expenseapproval.domain.model.ExpenseClaimApproved;
import io.github.xfoundries.demo.expenseapproval.domain.model.ExpenseClaimId;
import io.github.xfoundries.demo.expenseapproval.domain.model.Money;
import io.github.xfoundries.demo.expenseapproval.domain.model.UserId;
import io.huahill.jfoundry.application.event.externalization.ExternalizedEvent;
import org.junit.jupiter.api.Test;

class ExpenseClaimApprovedExternalizerTest {

    @Test
    void externalizesApprovalWithTheStableIntegrationContract() {
        Instant approvedAt = Instant.parse("2026-07-27T12:00:00Z");
        ExpenseClaimApproved event = new ExpenseClaimApproved(
                new ExpenseClaimId("claim-1"), new UserId("user-1"), Money.cny("42.50"), approvedAt);

        ExternalizedEvent externalized = new ExpenseClaimApprovedExternalizer()
                .externalize(event)
                .getFirst();

        assertThat(externalized.topic()).isEqualTo("expense-approval.events.v1");
        assertThat(externalized.payloadType()).isEqualTo("expense-approval.claim-approved.v1");
        assertThat(externalized.payloadKey()).isEqualTo("claim-1");
        assertThat(externalized.aggregateType()).isEqualTo("ExpenseClaim");
        assertThat(externalized.aggregateId()).isEqualTo("claim-1");
        assertThat(externalized.payload()).isInstanceOf(EventEnvelope.class);

        @SuppressWarnings("unchecked")
        EventEnvelope<ExpenseClaimApprovedV1> envelope = (EventEnvelope<ExpenseClaimApprovedV1>) externalized.payload();
        assertThat(envelope.eventId()).isEqualTo(event.getEventId().toString());
        assertThat(envelope.occurredAt()).isEqualTo(approvedAt);
        assertThat(envelope.payload()).isEqualTo(new ExpenseClaimApprovedV1(
                "claim-1", "user-1", new BigDecimal("42.50"), "CNY", approvedAt));
    }
}
