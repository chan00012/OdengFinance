package com.odeng.finance.auth.domain.model

data class AuthZ(
    val user: User,
    val userGroups: List<UserGroup>
)