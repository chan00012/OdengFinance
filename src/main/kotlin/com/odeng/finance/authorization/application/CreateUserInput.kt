package com.odeng.finance.authorization.application

/**
 * Application layer input DTO for creating a user.
 * This represents the command/use case input.
 */
data class CreateUserInput(
    val username: String,
    val email: String,
    val password: String
) {
    override fun toString(): String {
        return "CreateUserInput(username='$username', email='$email')"
    }
}

