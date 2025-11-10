package com.odeng.finance.authorization.domain

interface HashService {
    fun hash(raw: String): String
    fun matches(raw: String, hashed: String): Boolean
}