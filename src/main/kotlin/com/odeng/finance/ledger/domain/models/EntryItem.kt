package com.odeng.finance.ledger.domain.models

import com.odeng.finance.common.Money

class EntryItem(
    val id: Long,
    val journalId: Long,
    val accountId: Long,
    val billingAmount: Money,
    val direction: Direction
) {
}