package com.odeng.finance.auth.infrastructure.impl

import com.odeng.finance.auth.infrastructure.HashService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

/**
 * BCrypt implementation of HashService.
 * Uses Spring Security's BCryptPasswordEncoder for password hashing.
 */
@Service
class BCryptHashService : HashService {
    private val passwordEncoder = BCryptPasswordEncoder()

    override fun hash(raw: String): String {
        return passwordEncoder.encode(raw)
    }

    override fun matches(raw: String, hashed: String): Boolean {
        return passwordEncoder.matches(raw, hashed)
    }
}

