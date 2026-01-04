package com.odeng.finance.auth.infrastructure

import java.time.Instant

data class TokenResult(
    val token: String,
    val expiresAt: Instant
)

