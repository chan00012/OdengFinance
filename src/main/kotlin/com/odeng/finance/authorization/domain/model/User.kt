package com.odeng.finance.authorization.domain.model

class User(
    val id: Long? = null,
    val userName: String,
    val email: String,
    val hashPassword: String,
    val status: UserStatus,
) {
}