package com.odeng.finance.ledger.domain.model

import java.time.Instant

/**
 * JournalEntry domain entity.
 * Represents a journal entry with multiple line items (debits and credits).
 */
data class JournalEntry(
    val id: Long? = null,
    val description: String,
    val memo: String,
    val items: List<EntryItem> = emptyList(),
    val transactionDate: Instant,
    val createdOn: Instant? = null,
) {
    /**
     * Creates a new JournalEntry with an additional item.
     * This maintains immutability by creating a new instance.
     */
    fun addItem(item: EntryItem): JournalEntry {
        return copy(items = items + item)
    }
}