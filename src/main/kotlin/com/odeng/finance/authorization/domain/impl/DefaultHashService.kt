package com.odeng.finance.authorization.domain.impl

import com.odeng.finance.authorization.domain.HashService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class DefaultHashService : HashService {
    private final val passwordEncoder = BCryptPasswordEncoder()

    override fun hash(raw: String): String {
        return passwordEncoder.encode(raw)
    }

    override fun matches(raw: String, hashed: String): Boolean {
        return passwordEncoder.matches(raw, hashed)
    }
}