package com.odeng.finance.auth.domain.model

data class UserRole(
    val id: Long,
    val userId: Long,
    val access: AccessType
) {
}