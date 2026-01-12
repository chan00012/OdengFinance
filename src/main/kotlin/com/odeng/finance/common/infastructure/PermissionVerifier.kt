package com.odeng.finance.common.infastructure

import com.odeng.finance.auth.domain.model.AccessType
import com.odeng.finance.auth.domain.model.UserGroup
import com.odeng.finance.ledger.application.AccountService
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service("permission")
class PermissionVerifier(
    private val accountService: AccountService,
    private val currentAuthzContext: CurrentAuthzContext
) {

    private companion object {
        val logger = KotlinLogging.logger {}
    }

    fun isSameUser(userId: Long): Boolean {
        val authz = currentAuthzContext.get()
        return userId == authz.user.id
    }

    fun canShareAccount(accountId: Long): Boolean =
        hasAccountPermission(accountId) { it.canShare() }

    fun canReadAccount(accountId: Long): Boolean =
        hasAccountPermission(accountId) { it.canRead() }

    fun canWriteAccount(accountId: Long): Boolean =
        hasAccountPermission(accountId) { it.canWrite() }

    fun canDeleteAccount(accountId: Long): Boolean =
        hasAccountPermission(accountId) { it.canDelete() }

    private fun hasAccountPermission(accountId: Long, permissionCheck: (AccessType) -> Boolean): Boolean {
        val authz = currentAuthzContext.get()
        val account = accountService.getByAccountId(accountId)

        return authz.userGroups
            .filter { it.id == account.userGroupId }
            .flatMap { it.userRoles }
            .any { it.userId == authz.user.id && permissionCheck(it.access) }
    }
}