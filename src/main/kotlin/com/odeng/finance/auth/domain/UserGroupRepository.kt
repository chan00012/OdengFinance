package com.odeng.finance.auth.domain

import com.odeng.finance.auth.domain.model.UserGroup

interface UserGroupRepository {
    fun create(userId: Long): UserGroup
}