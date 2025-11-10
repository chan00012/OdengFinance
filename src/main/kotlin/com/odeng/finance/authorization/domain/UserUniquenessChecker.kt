package com.odeng.finance.authorization.domain

/**
 * Domain port for checking uniqueness of user attributes.
 * This is a domain interface that will be implemented by infrastructure layer.
 */
interface UserUniquenessChecker {
    /**
     * Checks if a username is available (not already taken).
     * @param username The username to check
     * @return true if username is available, false if already taken
     */
    fun isUsernameAvailable(username: String): Boolean

    /**
     * Checks if an email is available (not already taken).
     * @param email The email to check
     * @return true if email is available, false if already taken
     */
    fun isEmailAvailable(email: String): Boolean
}

