package com.odeng.finance.auth.domain.model

data class UserGroup(
    val id: Long,
    val roleUsers: List<RoleUser>
) {
}