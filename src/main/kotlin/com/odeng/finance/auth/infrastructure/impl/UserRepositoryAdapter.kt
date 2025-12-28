package com.odeng.finance.auth.infrastructure.impl

import com.odeng.finance.auth.domain.UserRepository
import com.odeng.finance.auth.domain.model.User
import com.odeng.finance.auth.infrastructure.entities.JpaUser
import com.odeng.finance.auth.infrastructure.entities.JpaUser.Companion.toDomain
import com.odeng.finance.auth.infrastructure.repository.JpaUserRepository
import org.springframework.stereotype.Repository

/**
 * Infrastructure adapter that implements the domain UserRepository port using JPA.
 */
@Repository
class UserRepositoryAdapter(
    private val jpaUserRepository: JpaUserRepository
) : UserRepository {

    private companion object {
        fun User.toEntity(): JpaUser {
            return JpaUser(
                id = id,
                username = username,
                email = email,
                hashPassword = hashPassword,
                status = status,
            )
        }
    }

    override fun create(user: User): User {
        val entity = user.toEntity()
        val savedEntity = jpaUserRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findByEmail(email: String): User? {
        return jpaUserRepository.findByEmail(email)?.toDomain()
    }
}

