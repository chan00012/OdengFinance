package com.odeng.finance.authorization.domain

import com.odeng.finance.authorization.domain.model.User

interface UserRepository {
    fun create(user: User): User
}