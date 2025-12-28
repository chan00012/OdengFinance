package com.odeng.finance.auth.domain.model

data class RoleUser(
    val id: Long,
    val access: AccessType
) {
}