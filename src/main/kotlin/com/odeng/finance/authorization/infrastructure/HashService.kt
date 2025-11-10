package com.odeng.finance.authorization.infrastructure

/**
 * Infrastructure service for hashing and verifying passwords.
 */
interface HashService {
    fun hash(raw: String): String

    fun matches(raw: String, hashed: String): Boolean
}

