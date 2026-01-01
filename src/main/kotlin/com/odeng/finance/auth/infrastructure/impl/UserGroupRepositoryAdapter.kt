package com.odeng.finance.auth.infrastructure.impl

import com.odeng.finance.auth.domain.UserGroupRepository
import com.odeng.finance.auth.domain.model.AccessType
import com.odeng.finance.auth.domain.model.UserGroup
import com.odeng.finance.auth.infrastructure.entities.JpaUserGroup
import com.odeng.finance.auth.infrastructure.entities.JpaUserRole
import com.odeng.finance.auth.infrastructure.entities.JpaUserRole.Companion.toDomain
import com.odeng.finance.auth.infrastructure.repository.JpaUserGroupRepository
import com.odeng.finance.auth.infrastructure.repository.JpaUserRoleRepository
import mu.KotlinLogging
import org.springframework.stereotype.Repository

@Repository
class UserGroupRepositoryAdapter(
    private val jpaUserGroupRepository: JpaUserGroupRepository,
    private val jpaUserRoleRepository: JpaUserRoleRepository
) : UserGroupRepository {
    private companion object {
        val logger = KotlinLogging.logger {}
    }

    override fun create(userId: Long): UserGroup {
        val savedUserGroupEntity = jpaUserGroupRepository.save(JpaUserGroup())

        val userRoleEntity = JpaUserRole(
            userId = userId,
            userGroupId = savedUserGroupEntity.id!!,
            accessType = AccessType.OWNER
        )
        val savedUserRoleEntity = jpaUserRoleRepository.save(userRoleEntity)

        return UserGroup(
            id = savedUserGroupEntity.id!!,
            userRoles = listOf(savedUserRoleEntity.toDomain())
        ).also {
            logger.info { "User group created: $it" }
        }
    }
}