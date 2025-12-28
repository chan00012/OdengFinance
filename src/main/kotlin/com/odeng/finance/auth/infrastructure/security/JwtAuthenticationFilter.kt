package com.odeng.finance.auth.infrastructure.security

import com.odeng.finance.auth.domain.model.User
import com.odeng.finance.auth.infrastructure.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val tokenService: TokenService<User>
) : OncePerRequestFilter() {
    private companion object {
        val log = KotlinLogging.logger {}
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)

            if (tokenService.validate(token)) {
                try {
                    val user = tokenService.parse(token)

                    val authentication = UsernamePasswordAuthenticationToken(
                        user.username,
                        null,
                        emptyList() // Authorities/roles can be added here later
                    )

                    SecurityContextHolder.getContext().authentication = authentication
                } catch (e: Exception) {
                    log.warn { "Failed to parse JWT token: ${e.message}" }
                }
            } else {
                log.warn { "Invalid JWT token" }
            }
        }

        filterChain.doFilter(request, response)
    }
}