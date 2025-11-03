package com.odeng.finance.ledger.domain.impl

import com.odeng.finance.ledger.domain.EntryItem
import com.odeng.finance.ledger.domain.EntryItemRepository
import com.odeng.finance.ledger.infastructure.entities.JpaEntryItem
import com.odeng.finance.ledger.infastructure.entities.JpaEntryItem.Companion.toDomain
import com.odeng.finance.ledger.infastructure.repository.JpaEntryItemRepository
import org.springframework.stereotype.Repository

@Repository
class DefaultEntryItemRepository(
    private val jpaEntryItemRepository: JpaEntryItemRepository
): EntryItemRepository {
    private companion object {
        fun EntryItem.toEntity(): JpaEntryItem {
            return JpaEntryItem(
                id = id,
                accountId = accountId,
                journalId = journalId,
                billingAmount = billingAmount.amount,
                currency = billingAmount.currency,
                direction = direction
            )
        }
    }
    override fun create(entryItem: EntryItem): EntryItem {
        val entity = entryItem.toEntity()
        val savedEntity = jpaEntryItemRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun getByJournalId(journalId: Long): List<EntryItem> {
        return jpaEntryItemRepository.findByJournalId(journalId).map { it.toDomain() }
    }

    override fun getByAccountId(accountId: Long): List<EntryItem> {
        return jpaEntryItemRepository.findByAccountId(accountId).map { it.toDomain() }
    }
}