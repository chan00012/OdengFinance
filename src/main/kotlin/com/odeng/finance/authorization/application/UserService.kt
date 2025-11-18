package com.odeng.finance.authorization.application

import com.odeng.finance.authorization.domain.CreateUserInputValidator
import com.odeng.finance.authorization.domain.UserAggregate
import com.odeng.finance.authorization.domain.UserRepository
import com.odeng.finance.authorization.domain.model.User
import com.odeng.finance.authorization.infrastructure.HashService
import com.odeng.finance.common.ValidationException
import mu.KotlinLogging
import org.springframework.stereotype.Service

/**
 * Application service for user-related use cases.
 * Orchestrates domain logic, validation, and infrastructure services.
 * This is the Application Layer in DDD/Hexagonal Architecture.
 */
@Service
class UserService(
    private val validator: CreateUserInputValidator,
    private val hashService: HashService,
    private val userRepository: UserRepository
) {
    private companion object {
        val logger = KotlinLogging.logger {}
    }

    /**
     * Creates a new user.
     * Orchestrates validation, password hashing, domain logic, and persistence.
     */
    fun createUser(input: CreateUserInput): User {
        logger.info { "Creating user with input=$input" }

        // Step 1: Validate input
        val validationResult = validator.validate(input)
        if (!validationResult.isValid) {
            logger.warn { "User creation validation failed: ${validationResult.errors}" }
            throw ValidationException(validationResult.errors)
        }

        // Step 2: Hash password
        val hashedPassword = hashService.hash(input.password)

        // Step 3: Create user through domain aggregate
        val aggregate = UserAggregate()
        val user = aggregate.createUser(
            username = input.username,
            email = input.email,
            hashedPassword = hashedPassword
        )

        // Step 4: Persist user
        val savedUser = userRepository.create(user)

        logger.info { "User created successfully: username=${savedUser.username}, id=${savedUser.id}" }
        return savedUser
    }

    fun initiateVerification() {

    }
}

