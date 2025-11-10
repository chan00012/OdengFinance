package com.odeng.finance.common

data class ValidationResult(
    val isValid: Boolean,
    val errors: List<String>
) {
    companion object {
        fun ok(): ValidationResult {
            return ValidationResult(
                isValid = true,
                errors = listOf()
            )
        }

        fun failed(error: String): ValidationResult {
            return ValidationResult(
                isValid = false,
                errors = listOf(error)
            )
        }

        fun failed(errors: List<String>): ValidationResult {
            return ValidationResult(
                isValid = false,
                errors = errors
            )
        }
    }
}
