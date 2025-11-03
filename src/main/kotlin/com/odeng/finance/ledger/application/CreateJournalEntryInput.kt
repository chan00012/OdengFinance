package com.odeng.finance.ledger.application

import com.odeng.finance.common.Money
import java.time.Instant

data class CreateJournalEntryInput(
    val amount: Money,
    val description: String,
    val memo: String,
    val transactionDate: Instant,
    val sourceAccountId: Long,
    val destinationAccountId: Long
)
