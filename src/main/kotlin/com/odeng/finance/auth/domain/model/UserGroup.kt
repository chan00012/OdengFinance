package com.odeng.finance.auth.domain.model

data class UserGroup(
    val id: Long,
    val userRoles: List<UserRole>
) {
    fun isOwner(id: Long): Boolean {
        return this.userRoles.any { it.userId == id && it.access == AccessType.OWNER }
    }

    fun getUserAccessType(userId: Long): AccessType {
        return this.userRoles.find { it.userId == userId }!!.access
    }
}