package com.odeng.finance.ledger.application

import com.odeng.finance.ledger.domain.model.Account

interface AccountService {

    fun create(input: CreateAccountInput): Account

    fun getByAccountId(id: Long): Account?

    fun getByUserId(userId: Long): List<Account>
}