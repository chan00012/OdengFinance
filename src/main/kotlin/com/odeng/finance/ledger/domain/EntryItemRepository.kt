package com.odeng.finance.ledger.domain

import com.odeng.finance.ledger.domain.model.EntryItem

interface EntryItemRepository {
    fun create(entryItem: EntryItem): EntryItem

    fun getByJournalId(journalId: Long): List<EntryItem>

    fun getByAccountId(accountId: Long): List<EntryItem>
}