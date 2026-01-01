package com.odeng.finance.auth.application

import com.odeng.finance.auth.domain.model.User

interface UserService {

    fun create(input: CreateUserInput): User
}