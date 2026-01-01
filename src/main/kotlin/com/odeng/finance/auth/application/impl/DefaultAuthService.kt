package com.odeng.finance.auth.application.impl

import com.odeng.finance.auth.application.AuthException
import com.odeng.finance.auth.application.AuthService
import com.odeng.finance.auth.application.AuthenticationInput
import com.odeng.finance.auth.domain.UserRepository
import com.odeng.finance.auth.domain.model.AuthN
import com.odeng.finance.auth.domain.model.AuthZ
import com.odeng.finance.auth.infrastructure.HashService
import com.odeng.finance.auth.infrastructure.impl.AuthenticationTokenService
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class DefaultAuthService(
    private val userRepository: UserRepository,
    private val hashService: HashService,
    private val authenticationTokenService: AuthenticationTokenService
) : AuthService {
    private companion object {
        val logger = KotlinLogging.logger {}
    }

    override fun authenticate(input: AuthenticationInput): AuthN {
        logger.info { "Authenticating user: ${input.email}" }

        val user = userRepository.findByEmail(input.email)

        if (user == null) {
            logger.error { "User not found for email: ${input.email}" }
            throw AuthException.Companion.UNAUTHORIZED
        }

        if (!hashService.matches(input.password, user.hashPassword)) {
            logger.error("Incorrect password for user: ${input.email}")
            throw AuthException.Companion.UNAUTHORIZED
        }

        return AuthN(
            token = authenticationTokenService.generate(user)
        )
    }

    override fun authorize(token: String): AuthZ {
        if (!authenticationTokenService.validate(token)) {
            logger.error { "Invalid JWT token" }
            throw AuthException.Companion.FORBIDDEN
        }

        return try {
            val userClaim = authenticationTokenService.parse(token)
            val user = userRepository.findByUserId(userClaim.id!!)!!

            AuthZ(
                user = user,
                userGroups = emptyList()
            )
        } catch (e: Exception) {
            logger.error { "Failed to parse JWT token: ${e.message}" }
            throw AuthException.Companion.FORBIDDEN
        }
    }
}