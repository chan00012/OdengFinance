package com.odeng.finance.auth.domain.impl

import com.odeng.finance.auth.domain.CreateUserInputValidator
import com.odeng.finance.auth.domain.UserInputValidator
import com.odeng.finance.auth.domain.UserUniquenessChecker
import com.odeng.finance.common.ValidationResult
import org.springframework.stereotype.Component

/**
 * Composite validator that combines pure domain validation with infrastructure-based uniqueness checking.
 */
@Component
class DefaultCreateUserInputValidator(
    private val uniquenessChecker: UserUniquenessChecker
) : CreateUserInputValidator {

    private val domainValidator = UserInputValidator()

    override fun validateUsername(username: String): ValidationResult = when {
        !uniquenessChecker.isUsernameAvailable(username) -> ValidationResult.failed("Username is already taken")
        else -> ValidationResult.ok()
    }

    override fun validateEmail(email: String): ValidationResult {
        // First check format (domain rule)
        val formatResult = domainValidator.validateEmailFormat(email)
        if (!formatResult.isValid) {
            return formatResult
        }

        // Then check uniqueness (infrastructure check)
        return when {
            !uniquenessChecker.isEmailAvailable(email) -> ValidationResult.failed("Email is already associated with another user")
            else -> ValidationResult.ok()
        }
    }

    override fun validatePassword(password: String): ValidationResult {
        return domainValidator.validatePassword(password)
    }
}