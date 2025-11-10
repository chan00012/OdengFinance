package com.odeng.finance.authorization.infrastructure.impl

import com.odeng.finance.authorization.domain.UserUniquenessChecker
import com.odeng.finance.authorization.infrastructure.repository.JpaUserRepository
import org.springframework.stereotype.Component

/**
 * Infrastructure implementation of UserUniquenessChecker using JPA repository.
 */
@Component
class JpaUserUniquenessChecker(
    private val jpaUserRepository: JpaUserRepository
) : UserUniquenessChecker {

    override fun isUsernameAvailable(username: String): Boolean {
        return !jpaUserRepository.existsByUsername(username)
    }

    override fun isEmailAvailable(email: String): Boolean {
        return !jpaUserRepository.existsByEmail(email)
    }
}

