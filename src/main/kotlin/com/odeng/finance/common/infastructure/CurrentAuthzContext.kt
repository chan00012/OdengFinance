package com.odeng.finance.common.infastructure

import com.odeng.finance.auth.application.AuthException
import com.odeng.finance.auth.domain.model.AuthZ
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class CurrentAuthzContext {

    fun get(): AuthZ {
        return SecurityContextHolder.getContext()
            .authentication
            ?.principal as? AuthZ
            ?: throw AuthException.Companion.FORBIDDEN
    }
}