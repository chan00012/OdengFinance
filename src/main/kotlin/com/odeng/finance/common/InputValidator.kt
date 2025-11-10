package com.odeng.finance.common

interface InputValidator<INPUT> {
    fun validate(input: INPUT): ValidationResult
}