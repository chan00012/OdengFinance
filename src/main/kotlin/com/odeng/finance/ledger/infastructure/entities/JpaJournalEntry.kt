package com.odeng.finance.ledger.infastructure.entities

import com.odeng.finance.ledger.domain.model.JournalEntry
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
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
@Table(schema = "ledger", name = "journal_entry")
@SequenceGenerator(name = "journal_entry_seq_generator", sequenceName = "ledger.journal_entry_seq", allocationSize = 1)
class JpaJournalEntry(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "journal_entry_seq_generator")
    val id: Long? = null,

    @Column(name = "description", nullable = false)
    val description: String,

    @Column(name = "memo")
    val memo: String,

    @Column(name = "transaction_date", nullable = false)
    val transactionDate: Instant,

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
        fun JpaJournalEntry.toDomain(): JournalEntry {
            return JournalEntry(
                id = id,
                description = description,
                memo = memo,
                transactionDate = transactionDate,
                createdOn = createdOn!!,
                items = mutableListOf()
            )
        }
    }
}