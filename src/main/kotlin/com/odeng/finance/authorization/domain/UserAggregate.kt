package com.odeng.finance.authorization.domain

import com.odeng.finance.authorization.domain.model.User
import com.odeng.finance.authorization.domain.model.UserStatus
import com.odeng.finance.common.ValidationException

class UserAggregate(
    var user: User? = null
) {

    fun createUser(
        input: CreateUserInput,
        validator: CreateUserInputValidator,
        hashService: HashService,
        userRepository: UserRepository
    ) {

        val validationResult = validator.validate(input)
        if (!validationResult.isValid) {
            throw ValidationException(validationResult.errors)
        }

        val user = User(
            username = input.username,
            email = input.email,
            hashPassword = hashService.hash(input.password),
            status = UserStatus.CREATED
        )

        val createdUser = userRepository.create(user)
        this.user = createdUser
    }
}