package com.odeng.finance.auth.infrastructure.repository

import com.odeng.finance.auth.infrastructure.entities.JpaUserRole
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRoleRepository : JpaRepository<JpaUserRole, Long> {
    fun findByUserId(userId: Long): List<JpaUserRole>

    fun findByUserGroupId(userGroupId: Long): List<JpaUserRole>

    fun findByUserGroupIdAndUserIdIn(userGroupId: Long, userIds: List<Long>): List<JpaUserRole>
}