package com.odeng.finance.ledger.domain.model

import com.odeng.finance.common.Money

/**
 * Account domain entity.
 * Represents a financial account in the ledger system.
 */
data class Account(
    val id: Long? = null,
    val userGroupId: Long,
    val name: String,
    val balance: Money? = null,
    val accountType: AccountType,
    val accountStatus: AccountStatus
)