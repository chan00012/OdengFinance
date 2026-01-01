package com.odeng.finance.auth.domain

import com.odeng.finance.auth.domain.model.UserGroup

interface UserGroupRepository {
    fun create(userId: Long): UserGroup

    fun getByUserId(userId: Long): List<UserGroup>

    fun getByUserGroup(userGroupId: Long): UserGroup
}