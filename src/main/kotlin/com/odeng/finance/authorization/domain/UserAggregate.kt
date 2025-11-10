package com.odeng.finance.authorization.domain

import com.odeng.finance.authorization.domain.model.User
import com.odeng.finance.authorization.domain.model.UserStatus

/**
 * User Aggregate Root - contains pure domain logic for user lifecycle.
 * No infrastructure dependencies - follows DDD principles.
 *
 * Note: Now that User is a data class, we can use .copy() for state transitions.
 */
class UserAggregate(
    var user: User? = null
) {

    /**
     * Creates a new user with the given attributes.
     * Assumes validation and password hashing have already been performed by the application layer.
     *
     * @param username The username for the new user
     * @param email The email for the new user
     * @param hashedPassword The already-hashed password
     * @return The created User domain entity
     */
    fun createUser(
        username: String,
        email: String,
        hashedPassword: String
    ): User {
        val newUser = User(
            username = username,
            email = email,
            hashPassword = hashedPassword,
            status = UserStatus.CREATED
        )

        this.user = newUser
        return newUser
    }
}