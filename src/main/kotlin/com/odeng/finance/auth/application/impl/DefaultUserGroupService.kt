package com.odeng.finance.auth.application.impl

import com.odeng.finance.auth.application.AuthException
import com.odeng.finance.auth.application.UserGroupService
import com.odeng.finance.auth.application.UserGroupShareInput
import com.odeng.finance.auth.domain.UserGroupRepository
import com.odeng.finance.auth.domain.model.AccessType
import com.odeng.finance.auth.domain.model.UserGroup
import com.odeng.finance.common.BusinessException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
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
        logger.info { "Sharing userGroup: ${input.userGroupId} with userId: ${input.sharedWithUserId} with accessType: ${input.accessType}" }

        // Business Rule 1: Validate owner cannot share with themselves
        if (input.sharedWithUserId == input.ownerUserId) {
            logger.error { "Owner userId: ${input.ownerUserId} cannot share with themselves" }
            throw AuthException.ACCESS_DENIED
        }

        // Business Rule 2: Validate accessType is not OWNER
        if (input.accessType == AccessType.OWNER) {
            logger.error { "Cannot grant OWNER access through share API" }
            throw BusinessException("Cannot grant OWNER access", HttpStatus.BAD_REQUEST)
        }

        val userGroup = userGroupRepository.getByUserGroupId(input.userGroupId)
        val ownerUserRole = userGroup.getOwner()

        // Business Rule 3: Validate requester is the owner
        if (input.ownerUserId != ownerUserRole.userId) {
            logger.error { "UserId: ${input.ownerUserId} is not owner of userGroup: ${input.userGroupId}" }
            throw AuthException.ACCESS_DENIED
        }

        // Business Rule 4: Check if user already has access (duplicate check)
        val existingUser = userGroup.userRoles.find { it.userId == input.sharedWithUserId }
        if (existingUser != null) {
            logger.error { "UserId: ${input.sharedWithUserId} already has access to userGroup: ${input.userGroupId} with accessType: ${existingUser.access}" }
            throw BusinessException("Account is already shared with this user", HttpStatus.CONFLICT)
        }

        userGroupRepository.add(input.userGroupId, input.sharedWithUserId, input.accessType)
    }

    override fun getByUserId(userId: Long): List<UserGroup> {
        logger.info { "Getting userGroups for userId: $userId" }
        return userGroupRepository.getByUserId(userId)
    }
}