package com.odeng.finance.authorization.domain

import com.odeng.finance.authorization.domain.model.User
import com.odeng.finance.authorization.domain.model.UserStatus

/**
 * User Aggregate Root - contains pure domain logic for user lifecycle.
 */
class UserAggregate(
    var user: User? = null
) {

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