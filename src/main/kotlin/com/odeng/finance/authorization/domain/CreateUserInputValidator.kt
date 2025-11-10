package com.odeng.finance.authorization.domain

import com.odeng.finance.common.InputValidator
import com.odeng.finance.common.ValidationResult


interface CreateUserInputValidator : InputValidator<CreateUserInput> {

    override fun validate(input: CreateUserInput): ValidationResult {
        val usernameResult = validateUsername(input.username)
        val emailResult = validateEmail(input.email)
        val passwordResult = validatePassword(input.password)

        val resultList = listOf(usernameResult, emailResult, passwordResult)


        return ValidationResult(
            isValid = resultList.all { it.isValid },
            errors = resultList.flatMap { it.errors }
        )
    }

    fun validateUsername(username: String): ValidationResult

    fun validateEmail(email: String): ValidationResult

    fun validatePassword(password: String): ValidationResult

}