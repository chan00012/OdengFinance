package com.odeng.finance.ledger.application.impl

import com.odeng.finance.auth.application.UserGroupService
import com.odeng.finance.ledger.application.AccountService
import com.odeng.finance.ledger.application.CreateAccountInput
import com.odeng.finance.ledger.domain.AccountRepository
import com.odeng.finance.ledger.domain.model.Account
import com.odeng.finance.ledger.domain.model.AccountStatus
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class DefaultAccountService(
    private val accountRepository: AccountRepository,
    private val userGroupService: UserGroupService
) : AccountService {

    private companion object {
        val logger = KotlinLogging.logger {}
    }

    override fun create(input: CreateAccountInput): Account {
        val account = Account(
            userGroupId = input.userGroupId,
            name = input.name,
            accountType = input.accountType,
            accountStatus = AccountStatus.ACTIVE
        )

        val savedAccount = accountRepository.create(account)
        logger.info { "Account Created: $savedAccount" }
        return savedAccount
    }

    override fun getByAccountId(id: Long): Account? {
        return accountRepository.getById(id)
    }

    override fun getByUserId(userId: Long): List<Account> {
        val userGroups = userGroupService.findByUserId(userId)
        return accountRepository.getByUserGroupIds(userGroups.map { it.id })
    }
}