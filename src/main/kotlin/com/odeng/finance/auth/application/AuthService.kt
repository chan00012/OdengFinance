package com.odeng.finance.auth.application

import com.odeng.finance.auth.domain.UserRepository
import com.odeng.finance.auth.domain.model.AuthN
import com.odeng.finance.auth.domain.model.AuthZ
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

    fun authenticate(input: AuthenticationInput): AuthN {
        logger.info { "Authenticating user: ${input.email}" }

        val user = userRepository.findByEmail(input.email)

        if (user == null) {
            logger.error() { "User not found for email: ${input.email}" }
            throw AuthException.UNAUTHORIZED
        }

        if (!hashService.matches(input.password, user.hashPassword)) {
            logger.error("Incorrect password for user: ${input.email}")
            throw AuthException.UNAUTHORIZED
        }

        return AuthN(
            token = authenticationTokenService.generate(user)
        )
    }

    fun authorized(token: String): AuthZ {
        if (!authenticationTokenService.validate(token)) {
            logger.error { "Invalid JWT token" }
            throw AuthException.FORBIDDEN
        }

        return try {
            val userClaim = authenticationTokenService.parse(token)
            val user = userRepository.findByEmail(userClaim.email) ?: throw AuthException.FORBIDDEN

            AuthZ(
                user = user,
                userGroups = emptyList()
            )
        } catch (e: Exception) {
            logger.error { "Failed to parse JWT token: ${e.message}" }
            throw AuthException.FORBIDDEN
        }
    }
}