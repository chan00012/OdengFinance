package com.odeng.finance.ledger.domain

import com.odeng.finance.common.Money

class Account(
    val id: Long? = null,
    val userGroupId: Long,
    val name: String,
    val balance: Money? = null,
    val accountType: AccountType,
    val accountStatus: AccountStatus
) {
    override fun toString(): String {
        return "Account(id=$id, userGroupId=$userGroupId, name='$name', balance=$balance, accountType=$accountType, accountStatus=$accountStatus)"
    }
}