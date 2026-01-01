package com.odeng.finance.common.infastructure

import com.odeng.finance.auth.application.impl.DefaultAuthService
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
    fun jwtAuthenticationFilter(defaultAuthService: DefaultAuthService): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(defaultAuthService)
    }


    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtAuthenticationFilter: JwtAuthenticationFilter): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    // Auth endpoints - public
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/auth/v1/user/**").permitAll()

                    // Swagger/OpenAPI endpoints - public
                    .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()

                    // All other requests require authentication
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}

