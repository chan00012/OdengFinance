package com.odeng.finance.ledger.domain

interface EntryItemRepository {
    fun create(entryItem: EntryItem): EntryItem
}