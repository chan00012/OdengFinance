package com.odeng.finance.ledger.application

import com.odeng.finance.ledger.domain.Account
import com.odeng.finance.ledger.domain.AccountRepository
import com.odeng.finance.ledger.domain.AccountStatus
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {

    private companion object {
        val logger = KotlinLogging.logger {}
    }

    fun createAccount(input: CreateAccountInput): Account {
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

    fun getAccountById(id: Long): Account? {
        return accountRepository.getById(id)
    }
}