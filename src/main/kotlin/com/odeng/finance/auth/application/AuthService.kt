package com.odeng.finance.auth.application

import com.odeng.finance.auth.domain.UserRepository
import com.odeng.finance.auth.domain.model.Session
import com.odeng.finance.auth.infrastructure.HashService
import com.odeng.finance.auth.infrastructure.impl.AuthenticationTokenService
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val hashService: HashService,
    private val authenticationTokenService: AuthenticationTokenService

) {
    private companion object {
        val logger = KotlinLogging.logger {}
    }

    fun authenticate(input: AuthenticationInput): Session {
        logger.info { "Authenticating user: ${input.email}" }

        val user = userRepository.findByEmail(input.email)

        if (user == null) {
            logger.warn { "User not found for email: ${input.email}" }
            throw AuthException("Invalid credentials")
        }

        if (!hashService.matches(input.password, user.hashPassword)) {
            logger.warn("Incorrect password for user: ${input.email}")
            throw AuthException("Invalid credentials")
        }

        return Session(
            token = authenticationTokenService.generate(user)
        )
    }
}