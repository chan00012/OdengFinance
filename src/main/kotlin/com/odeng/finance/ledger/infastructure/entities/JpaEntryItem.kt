package com.odeng.finance.ledger.infastructure.entities

import com.odeng.finance.common.Currency
import com.odeng.finance.common.Money
import com.odeng.finance.ledger.domain.model.Direction
import com.odeng.finance.ledger.domain.model.EntryItem
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Audited
@EntityListeners(AuditingEntityListener::class)
@Table(schema = "ledger", name = "entry_item")
@SequenceGenerator(name = "entry_item_seq_generator", sequenceName = "ledger.entry_item_seq", allocationSize = 1)
class JpaEntryItem(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entry_item_seq_generator")
    val id: Long? = null,

    @Column(name = "account_id", nullable = false)
    val accountId: Long,

    @Column(name = "journal_id", nullable = false)
    val journalId: Long,

    @Column(name = "billing_amount", nullable = false)
    val billingAmount: Double,

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    val currency: Currency,

    @Column(name = "direction", nullable = false)
    @Enumerated(EnumType.STRING)
    val direction: Direction,

    @CreatedDate
    @Column(name = "created_on", updatable = false)
    var createdOn: Instant? = null,

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: Long? = null,

    @LastModifiedDate
    @Column(name = "updated_on")
    var updatedOn: Instant? = null,

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: Long? = null
) {

    companion object {
        fun JpaEntryItem.toDomain(): EntryItem {
            return EntryItem(
                id = id,
                journalId = journalId,
                accountId = accountId,
                billingAmount = Money(billingAmount, currency),
                direction = direction
            )
        }
    }
}