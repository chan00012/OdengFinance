package com.odeng.finance.ledger.interfaces.rest

import com.odeng.finance.auth.application.UserGroupService
import com.odeng.finance.auth.application.UserGroupShareInput
import com.odeng.finance.common.infastructure.CurrentAuthzContext
import com.odeng.finance.interfaces.rest.api.AccountApi
import com.odeng.finance.interfaces.rest.api.model.AccountListResponse
import com.odeng.finance.interfaces.rest.api.model.AccountResponse
import com.odeng.finance.interfaces.rest.api.model.CreateAccountRequest
import com.odeng.finance.interfaces.rest.api.model.ShareAccountRequest
import com.odeng.finance.interfaces.rest.api.model.ShareAccountResponse
import com.odeng.finance.ledger.application.AccountService
import com.odeng.finance.ledger.application.CreateAccountInput
import com.odeng.finance.ledger.domain.model.Account
import com.odeng.finance.ledger.domain.model.AccountStatus
import com.odeng.finance.ledger.domain.model.AccountType
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController
import com.odeng.finance.interfaces.rest.api.model.AccountStatus as RestAccountStatus
import com.odeng.finance.interfaces.rest.api.model.AccountType as RestAccountType

/**
 * REST controller implementation for Accounts API.
 *
 * This controller implements the AccountsApi interface generated from the OpenAPI specification.
 * It acts as an adapter between the REST API layer and the application service layer,
 * following the Hexagonal Architecture pattern.
 */
@RestController
class AccountsController(
    private val accountService: AccountService,
    private val userGroupService: UserGroupService,
    private val currentAuthzContext: CurrentAuthzContext
) : AccountApi {

    private companion object {
        val logger = KotlinLogging.logger {}
    }

    override fun createAccount(createAccountRequest: CreateAccountRequest): ResponseEntity<AccountResponse> {
        val authz = currentAuthzContext.get()
        logger.info { "Creating account with request: $createAccountRequest" }

        val userGroup = userGroupService.create(authz.user.id!!)
        val account = accountService.create(
            CreateAccountInput(
                userGroupId = userGroup.id,
                name = createAccountRequest.name,
                accountType = createAccountRequest.accountType.toDomain()
            )
        )

        val response = account.toApi()
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PreAuthorize("@permission.isResourceOwner(#userId)")
    override fun getAccounts(userId: Long): ResponseEntity<AccountListResponse> {
        logger.info { "Getting available accounts under userId: $userId" }
        val userGroups = userGroupService.getByUserId(userId)
        val accounts = accountService.getByUserGroupIds(userGroups.map { it.id })
        logger.info { "Available accounts for userId: $userId are: ${accounts.map { it.id }}" }

        val response = AccountListResponse(
            accounts = accounts.map { it.toApi() }
        )

        return ResponseEntity.ok(response)
    }

    override fun shareAccount(
        accountId: Long,
        shareAccountRequest: ShareAccountRequest
    ): ResponseEntity<Unit> {
        val authz = currentAuthzContext.get()
        val account = accountService.getByAccountId(accountId)
        userGroupService.share(UserGroupShareInput(
            userGroupId = account.userGroupId,
            ownerUserId = authz.user.id!!,
            sharedWithUserId = shareAccountRequest.userId
        ))

        return ResponseEntity.noContent().build()
    }


    // ============================================================================
    // Mapping Functions: API Models <-> Domain Models
    // ============================================================================

    private fun Account.toApi(): AccountResponse {
        return AccountResponse(
            id = id!!,
            name = name,
            accountType = accountType.toApi(),
            accountStatus = accountStatus.toApi()
        )
    }

    private fun RestAccountType.toDomain(): AccountType {
        return when (this) {
            RestAccountType.ASSET -> AccountType.ASSET
            RestAccountType.LIABILITY -> AccountType.LIABILITY
            RestAccountType.EQUITY -> AccountType.EQUITY
            RestAccountType.INCOME -> AccountType.INCOME
            RestAccountType.EXPENSE -> AccountType.EXPENSE
        }
    }

    private fun AccountType.toApi(): RestAccountType {
        return when (this) {
            AccountType.ASSET -> RestAccountType.ASSET
            AccountType.LIABILITY -> RestAccountType.LIABILITY
            AccountType.EQUITY -> RestAccountType.EQUITY
            AccountType.INCOME -> RestAccountType.INCOME
            AccountType.EXPENSE -> RestAccountType.EXPENSE
        }
    }

    private fun AccountStatus.toApi(): RestAccountStatus {
        return when (this) {
            AccountStatus.ACTIVE -> RestAccountStatus.ACTIVE
            AccountStatus.INACTIVE -> RestAccountStatus.INACTIVE
        }
    }
}

