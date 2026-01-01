package com.odeng.finance.auth.infrastructure.repository

import com.odeng.finance.auth.infrastructure.entities.JpaUserGroup
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserGroupRepository : JpaRepository<JpaUserGroup, Long> {
}