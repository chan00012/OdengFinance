package com.odeng.finance.auth.application.impl

import com.odeng.finance.auth.application.AuthException
import com.odeng.finance.auth.application.UserGroupService
import com.odeng.finance.auth.application.UserGroupShareInput
import com.odeng.finance.auth.domain.UserGroupRepository
import com.odeng.finance.auth.domain.model.UserGroup
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class DefaultUserGroupService(private val userGroupRepository: UserGroupRepository) : UserGroupService {
    private companion object {
        val logger = KotlinLogging.logger {}
    }

    override fun create(userId: Long): UserGroup {
        logger.info { "Creating new userGroup for userId: $userId" }
        return userGroupRepository.create(userId)

    }

    override fun share(input: UserGroupShareInput) {
        val validNewUserIds = input.newUserIds.filterNot { it == input.ownerUserId }
        logger.info { "Sharing userGroup: ${input.userGroupId} for userIds: $validNewUserIds" }
        val userGroup = userGroupRepository.getByUserGroupId(input.userGroupId)
        val ownerUserRole = userGroup.getOwner()

        if (input.ownerUserId != ownerUserRole.userId) {
            logger.error { "UserId: ${input.ownerUserId} is not owner of userGroup: ${input.userGroupId}" }
            throw AuthException.ACCESS_DENIED
        }

        userGroupRepository.add(input.userGroupId, validNewUserIds)
    }

    override fun getByUserId(userId: Long): List<UserGroup> {
        logger.info { "Getting userGroups for userId: $userId" }
        return userGroupRepository.getByUserId(userId)
    }
}