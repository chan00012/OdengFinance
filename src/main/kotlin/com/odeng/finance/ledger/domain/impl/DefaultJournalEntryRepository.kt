package com.odeng.finance.ledger.domain.impl

import com.odeng.finance.ledger.domain.JournalEntry
import com.odeng.finance.ledger.domain.JournalEntryRepository
import com.odeng.finance.ledger.infastructure.entities.JpaJournalEntry
import com.odeng.finance.ledger.infastructure.entities.JpaJournalEntry.Companion.toDomain
import com.odeng.finance.ledger.infastructure.repository.JpaJournalEntryRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Repository
class DefaultJournalEntryRepository(
    private val jpaJournalEntryRepository: JpaJournalEntryRepository
) : JournalEntryRepository {
    override fun createDraft(
        description: String,
        memo: String,
        transactionDate: Instant
    ): JournalEntry {
        val entity = JpaJournalEntry(
            description = description,
            memo = memo,
            transactionDate = transactionDate,
        )
        val savedEntity = jpaJournalEntryRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun getById(id: Long): JournalEntry {
        return jpaJournalEntryRepository.findById(id).getOrNull()?.toDomain() ?: error("Journal entry $id not found")
    }
}