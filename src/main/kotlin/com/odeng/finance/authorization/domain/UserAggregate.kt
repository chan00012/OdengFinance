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
        require(username.isNotBlank()) { "Username cannot be blank" }
        require(email.isNotBlank()) { "Email cannot be blank" }
        require(hashedPassword.isNotBlank()) { "Password cannot be blank" }

        val newUser = User(
            username = username,
            email = email,
            hashPassword = hashedPassword,
            status = UserStatus.CREATED
        )

        this.user = newUser
        return newUser
    }

    fun initiateVerification(): User {
        val aggregatedUser = requireNotNull(user) { "User not loaded on aggregate" }
        require(aggregatedUser.status == UserStatus.CREATED) {
            "Cannot initiate verification on user ${aggregatedUser.username} with status ${aggregatedUser.status}. " +
                    "Only CREATED status is allowed for verification"
        }

        aggregatedUser.copy(status = UserStatus.VERIFYING)
        this.user = aggregatedUser
        return aggregatedUser
    }

    fun completeVerification(): User {
        val aggregatedUser = requireNotNull(user) { "User not loaded on aggregate" }
        require(aggregatedUser.status == UserStatus.VERIFYING) {
            "Cannot complete verification on user ${aggregatedUser.username} with status ${aggregatedUser.status}" +
                    "Only VERIFYING status is allowed for completing verification"
        }

        aggregatedUser.copy(status = UserStatus.ACTIVE)
        this.user = aggregatedUser
        return aggregatedUser
    }
}