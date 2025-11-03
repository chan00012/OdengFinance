package com.odeng.finance.ledger.domain

import java.time.Instant

class JournalEntry(
    val id: Long? = null,
    val description: String,
    val memo: String,
    val items: MutableList<EntryItem> = mutableListOf(),
    val transactionDate: Instant,
    val createdOn: Instant? = null,
) {

    fun addItem(item: EntryItem) {
        items.add(item)
    }
}