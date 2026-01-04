package com.odeng.finance.auth.domain

import com.odeng.finance.auth.domain.model.AccessType
import com.odeng.finance.auth.domain.model.UserGroup

interface UserGroupRepository {
    fun create(userId: Long): UserGroup

    fun add(userGroupId: Long, sharedWithUserId: Long, accessType: AccessType)

    fun getByUserId(userId: Long): List<UserGroup>

    fun getByUserGroupId(userGroupId: Long): UserGroup
}