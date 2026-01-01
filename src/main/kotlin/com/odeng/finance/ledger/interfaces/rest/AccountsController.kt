package com.odeng.finance.ledger.interfaces.rest

import com.odeng.finance.auth.application.AuthException
import com.odeng.finance.auth.application.UserGroupService
import com.odeng.finance.auth.domain.model.AuthZ
import com.odeng.finance.common.infastructure.SecurityConfig
import com.odeng.finance.interfaces.rest.api.AccountsApi
import com.odeng.finance.interfaces.rest.api.model.AccountResponse
import com.odeng.finance.interfaces.rest.api.model.AccountStatus
import com.odeng.finance.interfaces.rest.api.model.AccountType
import com.odeng.finance.interfaces.rest.api.model.CreateAccountRequest
import com.odeng.finance.ledger.application.AccountService
import com.odeng.finance.ledger.application.CreateAccountInput
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
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
    private val userGroupService: UserGroupService
) : AccountsApi {

    private companion object {
        val logger = KotlinLogging.logger {}
    }

    /**
     * Creates a new account.
     *
     * Maps the API request to the domain input and converts the domain response back to API response.
     */
    override fun createAccount(createAccountRequest: CreateAccountRequest): ResponseEntity<AccountResponse> {
        logger.info { "Creating account: ${createAccountRequest.name}" }

        val authz = SecurityContextHolder.getContext()
            .authentication
            ?.principal as? AuthZ
            ?: throw AuthException.UNAUTHORIZED


        // Call domain service
        val userGroup = userGroupService.create(authz.user.id!!)
        val account = accountService.createAccount(
            CreateAccountInput(
                userGroupId = userGroup.id,
                name = createAccountRequest.name,
                accountType = mapAccountType(createAccountRequest.accountType)
            )
        )

        // Map domain response to API response
        val response = AccountResponse(
            id = account.id!!,
            userGroupId = account.userGroupId,
            name = account.name,
            accountType = mapAccountTypeToApi(account.accountType),
            accountStatus = mapAccountStatusToApi(account.accountStatus)
        )

        logger.info { "Account created successfully: ${response.id}" }
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    /**
     * Retrieves an account by its ID.
     */
    override fun getAccountById(accountId: Long): ResponseEntity<AccountResponse> {
        logger.info { "Fetching account with ID: $accountId" }

        val account = accountService.getAccountById(accountId)
            ?: return ResponseEntity.notFound().build()

        val response = AccountResponse(
            id = account.id!!,
            userGroupId = account.userGroupId,
            name = account.name,
            accountType = mapAccountTypeToApi(account.accountType),
            accountStatus = mapAccountStatusToApi(account.accountStatus)
        )

        logger.info { "Account retrieved successfully: ${response.id}" }
        return ResponseEntity.ok(response)
    }

    // ============================================================================
    // Mapping Functions: API Models <-> Domain Models
    // ============================================================================

    private fun mapAccountType(apiType: AccountType): com.odeng.finance.ledger.domain.model.AccountType {
        return when (apiType) {
            AccountType.ASSET -> com.odeng.finance.ledger.domain.model.AccountType.ASSET
            AccountType.LIABILITY -> com.odeng.finance.ledger.domain.model.AccountType.LIABILITY
            AccountType.EQUITY -> com.odeng.finance.ledger.domain.model.AccountType.EQUITY
            AccountType.INCOME -> com.odeng.finance.ledger.domain.model.AccountType.INCOME
            AccountType.EXPENSE -> com.odeng.finance.ledger.domain.model.AccountType.EXPENSE
        }
    }

    private fun mapAccountTypeToApi(domainType: com.odeng.finance.ledger.domain.model.AccountType): AccountType {
        return when (domainType) {
            com.odeng.finance.ledger.domain.model.AccountType.ASSET -> AccountType.ASSET
            com.odeng.finance.ledger.domain.model.AccountType.LIABILITY -> AccountType.LIABILITY
            com.odeng.finance.ledger.domain.model.AccountType.EQUITY -> AccountType.EQUITY
            com.odeng.finance.ledger.domain.model.AccountType.INCOME -> AccountType.INCOME
            com.odeng.finance.ledger.domain.model.AccountType.EXPENSE -> AccountType.EXPENSE
        }
    }

    private fun mapAccountStatusToApi(domainStatus: com.odeng.finance.ledger.domain.model.AccountStatus): AccountStatus {
        return when (domainStatus) {
            com.odeng.finance.ledger.domain.model.AccountStatus.ACTIVE -> AccountStatus.ACTIVE
            com.odeng.finance.ledger.domain.model.AccountStatus.INACTIVE -> AccountStatus.INACTIVE
        }
    }
}

