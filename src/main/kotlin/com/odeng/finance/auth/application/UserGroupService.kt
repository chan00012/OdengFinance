package com.odeng.finance.auth.application

import com.odeng.finance.auth.domain.model.UserGroup

interface UserGroupService {

    fun create(userId: Long): UserGroup
}