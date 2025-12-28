package com.odeng.finance.auth.application

data class AuthenticationInput(
    val email: String,
    val password: String,
)