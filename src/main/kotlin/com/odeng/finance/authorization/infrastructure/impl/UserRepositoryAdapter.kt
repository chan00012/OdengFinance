package com.odeng.finance.authorization.infrastructure.impl

import com.odeng.finance.authorization.domain.UserRepository
import com.odeng.finance.authorization.domain.model.User
import com.odeng.finance.authorization.infrastructure.entities.JpaUser
import com.odeng.finance.authorization.infrastructure.entities.JpaUser.Companion.toDomain
import com.odeng.finance.authorization.infrastructure.repository.JpaUserRepository
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
}

