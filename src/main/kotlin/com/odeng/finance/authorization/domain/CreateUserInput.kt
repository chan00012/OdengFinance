package com.odeng.finance.authorization.domain

data class CreateUserInput(
    val username: String,
    val email: String,
    val password: String
) {
    override fun toString(): String {
        return "CreateUserInput(username='$username', email='$email')"
    }
}