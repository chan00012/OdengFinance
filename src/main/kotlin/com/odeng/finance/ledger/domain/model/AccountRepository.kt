package com.odeng.finance.ledger.domain.model

interface AccountRepository {

    fun create(account: Account): Account

    fun getById(id: Long): Account
}