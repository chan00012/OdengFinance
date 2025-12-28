package com.odeng.finance.common.infastructure

import com.odeng.finance.auth.infrastructure.impl.AuthenticationTokenService
import com.odeng.finance.auth.infrastructure.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun jwtAuthenticationFilter(authenticationTokenService: AuthenticationTokenService): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(authenticationTokenService)
    }


    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtAuthenticationFilter: JwtAuthenticationFilter): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/auth/v1/user/**").permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}

