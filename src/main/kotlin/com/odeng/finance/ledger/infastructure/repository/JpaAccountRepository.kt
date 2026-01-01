package com.odeng.finance.ledger.infastructure.repository

import com.odeng.finance.ledger.infastructure.entities.JpaAccount
import org.springframework.data.jpa.repository.JpaRepository

interface JpaAccountRepository : JpaRepository<JpaAccount, Long> {
    fun findByUserGroupIdIn(userGroupIds: List<Long>): List<JpaAccount>
}