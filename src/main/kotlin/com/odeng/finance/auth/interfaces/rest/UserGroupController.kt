package com.odeng.finance.auth.interfaces.rest

import com.odeng.finance.auth.application.UserGroupService
import com.odeng.finance.auth.application.UserGroupShareInput
import com.odeng.finance.interfaces.rest.api.UserGroupApi
import com.odeng.finance.interfaces.rest.api.model.UserGroupShareRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

@RestController
class UserGroupController(private val userGroupService: UserGroupService): UserGroupApi {

    @PreAuthorize("@permission.isResourceOwner(#userGroupShareRequest.ownerUserId)")
    override fun shareUserGroup(userGroupShareRequest: UserGroupShareRequest): ResponseEntity<Unit> {
        userGroupService.share(userGroupShareRequest.toDomain())
        return ResponseEntity(Unit, HttpStatus.OK)
    }

    private fun UserGroupShareRequest.toDomain(): UserGroupShareInput {
        return UserGroupShareInput(
            userGroupId = this.userGroupId,
            ownerUserId = this.ownerUserId,
            newUserIds = this.newUserIds
        )
    }
}