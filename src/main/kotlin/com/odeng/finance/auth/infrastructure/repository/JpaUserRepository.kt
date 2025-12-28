package com.odeng.finance.auth.infrastructure.repository

import com.odeng.finance.auth.infrastructure.entities.JpaUser
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRepository : JpaRepository<JpaUser, Long> {
    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): JpaUser?
}