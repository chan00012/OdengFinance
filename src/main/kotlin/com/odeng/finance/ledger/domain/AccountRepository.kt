package com.odeng.finance.ledger.domain

import com.odeng.finance.ledger.domain.model.Account

interface AccountRepository {

    fun create(account: Account): Account

    fun getById(id: Long): Account
}