package com.odeng.finance.auth.domain.model

data class UserRole(
    val userId: Long,
    val access: AccessType
) {
}