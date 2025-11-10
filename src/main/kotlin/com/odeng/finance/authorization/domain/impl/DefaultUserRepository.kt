package com.odeng.finance.authorization.domain.impl

import com.odeng.finance.authorization.domain.UserRepository
import com.odeng.finance.authorization.domain.model.User
import com.odeng.finance.authorization.infastructure.entities.JpaUser
import com.odeng.finance.authorization.infastructure.entities.JpaUser.Companion.toDomain
import com.odeng.finance.authorization.infastructure.repository.JpaUserRepository
import org.springframework.stereotype.Repository

@Repository
class DefaultUserRepository(
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