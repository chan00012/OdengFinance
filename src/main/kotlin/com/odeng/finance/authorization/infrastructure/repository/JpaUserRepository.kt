package com.odeng.finance.authorization.infrastructure.repository

import com.odeng.finance.authorization.infrastructure.entities.JpaUser
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRepository : JpaRepository<JpaUser, Long> {
    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}