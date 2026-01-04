package com.odeng.finance.auth.domain.model

/**
 * User domain entity.
 * Represents a user in the system with authentication and status information.
 */
data class User(
    val id: Long? = null,
    val username: String,
    val email: String,
    val hashPassword: String,
    val status: UserStatus,
) {
    override fun toString(): String {
        return "User(id=$id, username='$username', email='$email', status=$status)"
    }
}