package com.odeng.finance.ledger.domain

import com.odeng.finance.ledger.domain.model.JournalEntry
import java.time.Instant

interface JournalEntryRepository {
    fun createDraft(description: String, memo: String, transactionDate: Instant): JournalEntry

    fun getById(id: Long): JournalEntry
}