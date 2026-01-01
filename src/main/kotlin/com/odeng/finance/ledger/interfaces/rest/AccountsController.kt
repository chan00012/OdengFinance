package com.odeng.finance.ledger.interfaces.rest

import com.odeng.finance.auth.application.UserGroupService
import com.odeng.finance.common.CurrentAuthzContext
import com.odeng.finance.interfaces.rest.api.AccountApi
import com.odeng.finance.interfaces.rest.api.model.AccountListResponse
import com.odeng.finance.interfaces.rest.api.model.AccountResponse
import com.odeng.finance.interfaces.rest.api.model.AccountStatus
import com.odeng.finance.interfaces.rest.api.model.AccountType
import com.odeng.finance.interfaces.rest.api.model.CreateAccountRequest
import com.odeng.finance.ledger.application.AccountService
import com.odeng.finance.ledger.application.CreateAccountInput
import com.odeng.finance.ledger.domain.model.Account
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

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
        logger.info { "Creating account: ${createAccountRequest.name}" }

        val userGroup = userGroupService.create(authz.user.id!!)
        val account = accountService.create(
            CreateAccountInput(
                userGroupId = userGroup.id,
                name = createAccountRequest.name,
                accountType = createAccountRequest.accountType.toDomain()
            )
        )

        val response = account.toApi()
        logger.info { "Account created successfully: ${response.id}" }
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    override fun getAccountsByUserId(userId: Long): ResponseEntity<AccountListResponse> {
        val accounts = accountService.getByUserId(userId)

        val response = AccountListResponse(
            accounts = accounts.map { it.toApi() }
        )

        return ResponseEntity.ok(response)
    }


    // ============================================================================
    // Mapping Functions: API Models <-> Domain Models
    // ============================================================================

    private fun Account.toApi(): AccountResponse {
        return AccountResponse(
            id = id!!,
            userGroupId = userGroupId,
            name = name,
            accountType = accountType.toApi(),
            accountStatus = accountStatus.toApi()
        )
    }

    private fun AccountType.toDomain(): com.odeng.finance.ledger.domain.model.AccountType {
        return when (this) {
            AccountType.ASSET -> com.odeng.finance.ledger.domain.model.AccountType.ASSET
            AccountType.LIABILITY -> com.odeng.finance.ledger.domain.model.AccountType.LIABILITY
            AccountType.EQUITY -> com.odeng.finance.ledger.domain.model.AccountType.EQUITY
            AccountType.INCOME -> com.odeng.finance.ledger.domain.model.AccountType.INCOME
            AccountType.EXPENSE -> com.odeng.finance.ledger.domain.model.AccountType.EXPENSE
        }
    }

    private fun com.odeng.finance.ledger.domain.model.AccountType.toApi(): AccountType {
        return when (this) {
            com.odeng.finance.ledger.domain.model.AccountType.ASSET -> AccountType.ASSET
            com.odeng.finance.ledger.domain.model.AccountType.LIABILITY -> AccountType.LIABILITY
            com.odeng.finance.ledger.domain.model.AccountType.EQUITY -> AccountType.EQUITY
            com.odeng.finance.ledger.domain.model.AccountType.INCOME -> AccountType.INCOME
            com.odeng.finance.ledger.domain.model.AccountType.EXPENSE -> AccountType.EXPENSE
        }
    }

    private fun com.odeng.finance.ledger.domain.model.AccountStatus.toApi(): AccountStatus {
        return when (this) {
            com.odeng.finance.ledger.domain.model.AccountStatus.ACTIVE -> AccountStatus.ACTIVE
            com.odeng.finance.ledger.domain.model.AccountStatus.INACTIVE -> AccountStatus.INACTIVE
        }
    }
}

