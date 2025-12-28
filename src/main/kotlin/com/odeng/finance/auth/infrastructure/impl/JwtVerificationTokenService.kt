package com.odeng.finance.auth.infrastructure.impl

import com.odeng.finance.auth.domain.model.User
import com.odeng.finance.auth.infrastructure.TokenService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtVerificationTokenService(
    private val validUntil: Long = 60000,
    private val secretKey: String = "your-256-bit-secret-key-change-this-in-production-please-make-it-secure"
) : TokenService<User> {
    override fun generate(body: User): String {
        val now = Date()
        val expireAt = Date(now.time + validUntil)
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

        return Jwts.builder()
            .subject(body.username)
            .issuedAt(now)
            .expiration(expireAt)
            .claims(
                mapOf(
                    "username" to body.username,
                    "email" to body.email,
                )
            )
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    override fun validate(token: String): Boolean {
        TODO("Not yet implemented")
    }


}