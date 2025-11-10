package com.odeng.finance.authorization.domain.application

import com.odeng.finance.authorization.domain.CreateUserInput
import com.odeng.finance.authorization.domain.CreateUserInputValidator
import com.odeng.finance.authorization.domain.HashService
import com.odeng.finance.authorization.domain.UserAggregate
import com.odeng.finance.authorization.domain.UserRepository
import com.odeng.finance.authorization.domain.model.User
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class UserService(
    private val validator: CreateUserInputValidator,
    private val hashService: HashService,
    private val userRepository: UserRepository
) {
    private companion object {
        val logger = KotlinLogging.logger {}
    }

    fun createUser(input: CreateUserInput): User {
        logger.info("Creating user with input=$input")
        val aggregate = UserAggregate()
        aggregate.createUser(input, validator, hashService, userRepository)

        return aggregate.user!!
    }
}