package com.odeng.finance.common.infastructure

import com.odeng.finance.ledger.application.AccountService
import org.springframework.stereotype.Service

@Service("permission")
class PermissionVerifier(
    private val accountService: AccountService,
    private val currentAuthzContext: CurrentAuthzContext) {

    fun isSameUser(userId: Long): Boolean {
        val authz = currentAuthzContext.get()
        return userId == authz.user.id
    }

    fun isAccountOwner(accountId: Long): Boolean {
        val authz = currentAuthzContext.get()
        val account = accountService.getByAccountId(accountId)
        val userGroupId = account.userGroupId

        return authz.userGroups
            .find { it.id == account.userGroupId }
            ?.getOwner()
            ?.userId == authz.user.id
    }
}