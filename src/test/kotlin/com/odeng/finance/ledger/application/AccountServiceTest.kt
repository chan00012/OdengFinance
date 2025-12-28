package com.odeng.finance.ledger.application

import com.odeng.finance.ledger.domain.model.AccountStatus
import com.odeng.finance.ledger.domain.model.AccountType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    @Autowired
    private lateinit var accountService: AccountService

    @Test
    fun `should create account and persist to database`() {
        // Given
        val input = CreateAccountInput(
            name = "Test Account",
            accountType = AccountType.ASSET,
            userGroupId = 1L
        )

        // When
        val createdAccount = accountService.createAccount(input)

        // Then
        assertThat(createdAccount).isNotNull
        assertThat(createdAccount.id).isNotNull()
        assertThat(createdAccount.name).isEqualTo("Test Account")
        assertThat(createdAccount.accountType).isEqualTo(AccountType.ASSET)
        assertThat(createdAccount.userGroupId).isEqualTo(1L)
        assertThat(createdAccount.accountStatus).isEqualTo(AccountStatus.ACTIVE)

        // Verify it's actually in the database
        val savedEntity = accountService.getAccountById(createdAccount.id!!)
        assertThat(savedEntity).isNotNull
        assertThat(savedEntity?.name).isEqualTo("Test Account")
        assertThat(savedEntity?.accountType).isEqualTo(AccountType.ASSET)
        assertThat(savedEntity?.accountStatus).isEqualTo(AccountStatus.ACTIVE)
        assertThat(savedEntity?.userGroupId).isEqualTo(1L)
    }
}

