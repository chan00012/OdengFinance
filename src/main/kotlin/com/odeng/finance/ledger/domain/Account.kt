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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Account

        if (id != other.id) return false
        if (userGroupId != other.userGroupId) return false
        if (name != other.name) return false
        if (accountType != other.accountType) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + userGroupId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (balance?.hashCode() ?: 0)
        result = 31 * result + accountType.hashCode()
        result = 31 * result + accountStatus.hashCode()
        return result
    }
}