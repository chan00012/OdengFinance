package com.odeng.finance.auth.application

import com.odeng.finance.auth.domain.model.AuthN
import com.odeng.finance.auth.domain.model.AuthZ

interface AuthService {

    fun authenticate(input: AuthenticationInput): AuthN

    fun authorize(token: String): AuthZ
}