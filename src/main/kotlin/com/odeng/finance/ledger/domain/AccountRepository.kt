package com.odeng.finance.ledger.domain

interface AccountRepository {

    fun create(account: Account): Account

    fun getById(id: Long): Account?
}