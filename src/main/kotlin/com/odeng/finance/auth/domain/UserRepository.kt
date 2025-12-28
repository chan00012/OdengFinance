package com.odeng.finance.auth.domain

import com.odeng.finance.auth.domain.model.User

interface UserRepository {
    fun create(user: User): User

    fun findByEmail(email: String): User?

    fun findByUsername(username: String): User?
}