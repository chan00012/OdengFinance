package com.odeng.finance.auth.infrastructure.impl

import com.odeng.finance.auth.domain.UserGroupRepository
import com.odeng.finance.auth.domain.model.AccessType
import com.odeng.finance.auth.domain.model.UserGroup
import com.odeng.finance.auth.infrastructure.entities.JpaUserGroup
import com.odeng.finance.auth.infrastructure.entities.JpaUserRole
import com.odeng.finance.auth.infrastructure.entities.JpaUserRole.Companion.toDomain
import com.odeng.finance.auth.infrastructure.repository.JpaUserGroupRepository
import com.odeng.finance.auth.infrastructure.repository.JpaUserRoleRepository
import com.odeng.finance.common.BusinessException
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
        val savedJpaUserGroup = jpaUserGroupRepository.save(JpaUserGroup())

        val jpaUserRole = JpaUserRole(
            userId = userId,
            userGroupId = savedJpaUserGroup.id!!,
            accessType = AccessType.OWNER
        )
        val savedJpaUserRole = jpaUserRoleRepository.save(jpaUserRole)

        return UserGroup(
            id = savedJpaUserGroup.id!!,
            userRoles = listOf(savedJpaUserRole.toDomain())
        ).also {
            logger.info { "UserGroup created: $it" }
        }
    }

    override fun add(
        userGroupId: Long,
        newUserIds: List<Long>,
    ) {
        val existingUserRoles = jpaUserRoleRepository.findByUserGroupIdAndUserIdIn(userGroupId, newUserIds)
        val validNewUserIds = newUserIds.subtract(existingUserRoles.map { it.userId }).toList()

        val jpaUserRoles = validNewUserIds.map {
            JpaUserRole(
                userId = it,
                userGroupId = userGroupId,
                accessType = AccessType.SHARED
            )
        }

        jpaUserRoleRepository.saveAll(jpaUserRoles)
    }

    override fun getByUserId(userId: Long): List<UserGroup> {
        val jpaUserRoles = jpaUserRoleRepository.findByUserId(userId)

        return jpaUserRoles.groupBy { it.userGroupId }.map {
            UserGroup(
                id = it.key,
                userRoles = it.value.map { ur -> ur.toDomain() }
            )
        }
    }

    override fun getByUserGroupId(userGroupId: Long): UserGroup {
        val userGroupEntity = jpaUserGroupRepository.findById(userGroupId)
            .orElseThrow { BusinessException("User group not found") }
        val roleUserEntities = jpaUserRoleRepository.findByUserGroupId(userGroupEntity.id!!)

        return UserGroup(
            id = userGroupEntity.id!!,
            userRoles = roleUserEntities.map { it.toDomain() }
        )
    }
}