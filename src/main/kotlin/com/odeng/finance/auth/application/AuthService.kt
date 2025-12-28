package com.odeng.finance.auth.application

import com.odeng.finance.auth.domain.UserRepository
import com.odeng.finance.auth.domain.model.Session
import com.odeng.finance.auth.infrastructure.HashService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val hashService: HashService

) {
    fun authenticate(input: AuthenticationInput): Session {
        val user = userRepository.findByEmail(input.email)

        if (user == null) {
            throw AuthException("Invalid credentials")
        }

        if (!hashService.matches(input.password, user.hashPassword)) {
            throw AuthException("Invalid credentials")
        }

        return Session(
            token = "login successful"
        )
    }
}