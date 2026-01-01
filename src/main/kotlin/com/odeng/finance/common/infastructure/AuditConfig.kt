package com.odeng.finance.common.infastructure

import com.odeng.finance.auth.domain.model.AuthZ
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Configuration
@EnableJpaAuditing
class AuditConfig {

    @Bean
    fun auditorProvider(): AuditorAware<Long> {
        return AuditorAware {
            val authz = SecurityContextHolder.getContext()
                .authentication
                ?.principal as? AuthZ
            Optional.of(authz?.user!!.id ?: -1)
        }
    }
}