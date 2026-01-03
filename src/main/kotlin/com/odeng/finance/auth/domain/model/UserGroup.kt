package com.odeng.finance.auth.domain.model

data class UserGroup(
    val id: Long,
    val userRoles: List<UserRole>
) {

    fun getOwner(): UserRole {
        return this.userRoles.find { it.access == AccessType.OWNER }!!
    }
}