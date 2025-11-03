package com.odeng.finance.common

data class Money(
    val amount: Double,
    val currency: Currency,
) {
    fun negate(): Money {
        return Money(
            amount = -amount,
            currency = currency
        )
    }
}