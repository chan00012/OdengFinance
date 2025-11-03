package com.odeng.finance.ledger.domain

import com.odeng.finance.common.Money

class EntryItem(
    val id: Long? = null,
    val journalId: Long,
    val accountId: Long,
    val billingAmount: Money,
    val direction: Direction
) {
}