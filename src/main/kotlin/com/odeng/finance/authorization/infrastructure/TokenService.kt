package com.odeng.finance.authorization.infrastructure

import com.odeng.finance.authorization.domain.model.User

interface TokenService<BODY> {
    fun generate(body: BODY): String
    fun validate(token: String): Boolean
}