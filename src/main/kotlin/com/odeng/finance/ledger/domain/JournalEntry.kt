package com.odeng.finance.ledger.domain

import java.time.Instant

class JournalEntry(
    val id: Long,
    val description: String,
    val memo: String,
    val items: List<EntryItem>,
    val transactionDate: Instant,
    val createdOn: Instant,
) {
}