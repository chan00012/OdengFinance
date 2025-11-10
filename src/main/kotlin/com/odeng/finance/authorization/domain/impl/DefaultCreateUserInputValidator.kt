package com.odeng.finance.authorization.domain.impl

import com.odeng.finance.authorization.domain.CreateUserInputValidator
import com.odeng.finance.authorization.infastructure.repository.JpaUserRepository
import com.odeng.finance.common.ValidationResult
import org.springframework.stereotype.Component

@Component
class DefaultCreateUserInputValidator(
    private val jpaUserRepository: JpaUserRepository
) : CreateUserInputValidator {
    private companion object {
        val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    }

    override fun validateUsername(username: String): ValidationResult = when {
        jpaUserRepository.existsByUsername(username) -> ValidationResult.failed("Username is already taken.")
        else -> ValidationResult.ok()
    }

    override fun validateEmail(email: String): ValidationResult = when {
        !EMAIL_REGEX.matches(email) -> ValidationResult.failed("Invalid email.")
        jpaUserRepository.existsByEmail(email) -> ValidationResult.failed("Email is already associated with another user.")
        else -> ValidationResult.ok()
    }

    override fun validatePassword(password: String): ValidationResult = when {
        password.length !in 7..31 -> ValidationResult.failed("Password must be 7-31 characters.")
        password.none { !it.isLetterOrDigit() } -> ValidationResult.failed("Password must contain special character.")
        password.none { it.isUpperCase() } -> ValidationResult.failed("Password must contain uppercase letter.")
        password.none { it.isLowerCase() } -> ValidationResult.failed("Password must contain lowercase letter.")
        password.any { it.isWhitespace() } -> ValidationResult.failed("Password cannot contain spaces.")
        else -> ValidationResult.ok()
    }
}