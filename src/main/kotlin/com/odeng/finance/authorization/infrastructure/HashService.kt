package com.odeng.finance.authorization.infrastructure

/**
 * Infrastructure service for hashing and verifying passwords.
 * This is a technical concern, not domain logic.
 */
interface HashService {
    /**
     * Hashes a raw password string.
     * @param raw The raw password to hash
     * @return The hashed password
     */
    fun hash(raw: String): String

    /**
     * Verifies if a raw password matches a hashed password.
     * @param raw The raw password to check
     * @param hashed The hashed password to compare against
     * @return true if the passwords match, false otherwise
     */
    fun matches(raw: String, hashed: String): Boolean
}

