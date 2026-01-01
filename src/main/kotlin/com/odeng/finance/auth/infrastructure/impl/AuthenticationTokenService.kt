package com.odeng.finance.auth.infrastructure.impl

import com.odeng.finance.auth.domain.model.User
import com.odeng.finance.auth.domain.model.UserStatus
import com.odeng.finance.auth.infrastructure.TokenService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthenticationTokenService(
    private val validUntil: Long = 60000,
    private val secretKey: String = "your-256-bit-secret-key-change-this-in-production-please-make-it-secure"
) : TokenService<User> {
    private companion object {
        val logger = KotlinLogging.logger { }
    }

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
                    "id" to body.id,
                    "username" to body.username,
                    "email" to body.email,
                    "status" to body.status,
                )
            )
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    override fun validate(token: String): Boolean {
        return try {
            val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)

            true
        } catch (e: Exception) {
            logger.error(e) { "Failed to validate JWT" }
            false
        }
    }

    override fun parse(token: String): User {
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray())
        val claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

        return User(
            id = claims.get("id", Number::class.java)?.toLong(),
            username = claims.get("username", String::class.java),
            email = claims.get("email", String::class.java),
            hashPassword = "",
            status = UserStatus.valueOf(claims.get("status", String::class.java)),
        )
    }
}