package com.odeng.finance.ledger.domain

interface EntryItemRepository {
    fun create(entryItem: EntryItem): EntryItem

    fun getByJournalId(journalId: Long): List<EntryItem>

    fun getByAccountId(accountId: Long): List<EntryItem>
}