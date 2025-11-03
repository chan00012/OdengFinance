package com.odeng.finance.common.infastructure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@Configuration
@EnableJpaAuditing
class AuditConfig {

    @Bean
    fun auditorProvider(): AuditorAware<Long> {
        return AuditorAware {
            // TODO: Replace with actual user ID from security context
            // For now, return a default user ID
            Optional.of(1L)
        }
    }
}