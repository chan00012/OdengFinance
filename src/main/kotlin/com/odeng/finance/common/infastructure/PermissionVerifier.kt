package com.odeng.finance.common.infastructure

import com.odeng.finance.auth.application.UserGroupService
import org.springframework.stereotype.Service

@Service("permission")
class PermissionVerifier(
    private val userGroupService: UserGroupService,
    private val currentAuthzContext: CurrentAuthzContext) {

    fun isResourceOwner(userId: Long): Boolean {
        val authz = currentAuthzContext.get()
        return userId == authz.user.id
    }
}