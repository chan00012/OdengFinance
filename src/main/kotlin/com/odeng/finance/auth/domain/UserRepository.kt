package com.odeng.finance.auth.domain

import com.odeng.finance.auth.domain.model.User

interface UserRepository {
    fun create(user: User): User

    fun findByUserId(id: Long): User?

    fun findByEmail(email: String): User?

    fun findByUsername(username: String): User?
}