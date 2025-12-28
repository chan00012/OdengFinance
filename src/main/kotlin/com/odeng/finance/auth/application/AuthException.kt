package com.odeng.finance.auth.application

import org.springframework.http.HttpStatus

class AuthException(message: String = "Invalid credentials", val httpStatus: HttpStatus) : RuntimeException(message) {

    companion object {
        val UNAUTHORIZED = AuthException("Unauthorized", HttpStatus.UNAUTHORIZED)
        val FORBIDDEN = AuthException("Forbidden", HttpStatus.FORBIDDEN)

    }
}