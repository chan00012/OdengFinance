package com.odeng.finance.auth.application.impl

import com.odeng.finance.auth.application.UserGroupService
import com.odeng.finance.auth.domain.UserGroupRepository
import com.odeng.finance.auth.domain.model.UserGroup
import org.springframework.stereotype.Service

@Service
class DefaultUserGroupService(private val userGroupRepository: UserGroupRepository) : UserGroupService {

    override fun create(userId: Long): UserGroup {
        return userGroupRepository.create(userId)

    }
}