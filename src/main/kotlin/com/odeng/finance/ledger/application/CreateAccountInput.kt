package com.odeng.finance.ledger.application

import com.odeng.finance.ledger.domain.model.AccountType

data class CreateAccountInput(
    val name: String,
    val accountType: AccountType,
    val userGroupId: Long
)