package com.odeng.finance.ledger.domain.model

import com.odeng.finance.common.Money

/**
 * EntryItem domain entity.
 * Represents a single line item in a journal entry (debit or credit).
 */
data class EntryItem(
    val id: Long? = null,
    val journalId: Long,
    val accountId: Long,
    val billingAmount: Money,
    val direction: Direction
)