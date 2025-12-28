package com.odeng.finance.auth.infrastructure

interface TokenService<BODY> {
    fun generate(body: BODY): String

    fun validate(token: String): Boolean

    fun parse(token: String): BODY
}