package com.odeng.finance.auth.application

data class UserGroupShareInput(
    val userGroupId: Long,
    val ownerUserId: Long,
    val newUserIds: List<Long>
)