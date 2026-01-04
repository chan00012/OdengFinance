package com.odeng.finance.auth.application

import com.odeng.finance.auth.domain.model.AccessType

data class UserGroupShareInput(
    val userGroupId: Long,
    val ownerUserId: Long,
    val sharedWithUserId: Long,
    val accessType: AccessType
)