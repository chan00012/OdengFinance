package com.odeng.finance.auth.domain

import com.odeng.finance.common.ValidationResult

/**
 * Pure domain validator for user input.
 * Contains only business rules with no infrastructure dependencies.
 */
class UserInputValidator {
    private companion object {
        val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    }

    /**
     * Validates email format according to business rules.
     */
    fun validateEmailFormat(email: String): ValidationResult = when {
        !EMAIL_REGEX.matches(email) -> ValidationResult.failed("Invalid email")
        else -> ValidationResult.ok()
    }

    /**
     * Validates password according to business rules:
     * - Length between 7-31 characters
     * - Must contain at least one special character
     * - Must contain at least one uppercase letter
     * - Must contain at least one lowercase letter
     * - Cannot contain spaces
     */
    fun validatePassword(password: String): ValidationResult = when {
        password.length !in 7..31 -> ValidationResult.failed("Password must be 7-31 characters")
        password.none { !it.isLetterOrDigit() } -> ValidationResult.failed("Password must contain special character")
        password.none { it.isUpperCase() } -> ValidationResult.failed("Password must contain uppercase letter")
        password.none { it.isLowerCase() } -> ValidationResult.failed("Password must contain lowercase letter")
        password.any { it.isWhitespace() } -> ValidationResult.failed("Password cannot contain spaces")
        else -> ValidationResult.ok()
    }
}

