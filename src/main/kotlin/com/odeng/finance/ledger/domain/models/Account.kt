package com.odeng.finance.ledger.domain.models

import com.odeng.finance.common.Money

class Account(
    val id: Long,
    val userGroupId: Long,
    val name: String,
    val balance: Money,
    val accountType: AccountType,
    val accountStatus: AccountStatus
) {
}