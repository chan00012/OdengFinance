package com.odeng.finance.ledger.domain.impl

import com.odeng.finance.ledger.domain.model.Account
import com.odeng.finance.ledger.domain.AccountRepository
import com.odeng.finance.ledger.infastructure.entities.JpaAccount
import com.odeng.finance.ledger.infastructure.entities.JpaAccount.Companion.toDomain
import com.odeng.finance.ledger.infastructure.repository.JpaAccountRepository
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class DefaultAccountRepository(
    private val jpaAccountRepository: JpaAccountRepository
) : AccountRepository {
    private companion object {
        fun Account.toEntity(): JpaAccount {
            return JpaAccount(
                id = id,
                userGroupId = userGroupId,
                name = name,
                accountType = accountType,
                accountStatus = accountStatus
            )
        }

    }

    override fun create(account: Account): Account {
        val entity = account.toEntity()
        val savedEntity = jpaAccountRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun getById(id: Long): Account {
        return jpaAccountRepository.findById(id).getOrNull()?.toDomain() ?: error("Account with id $id not found")
    }

    override fun getByUserGroupIds(userGroupIds: List<Long>): List<Account> {
        return jpaAccountRepository.findByUserGroupIdIn(userGroupIds).map { it.toDomain() }
    }
}