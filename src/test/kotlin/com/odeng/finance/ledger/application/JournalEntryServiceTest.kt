package com.odeng.finance.ledger.application

import com.odeng.finance.common.Currency
import com.odeng.finance.common.Money
import com.odeng.finance.ledger.domain.AccountType
import com.odeng.finance.ledger.domain.Direction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.Instant

@SpringBootTest
@ActiveProfiles("test")
class JournalEntryServiceTest {

    @Autowired
    private lateinit var journalEntryService: JournalEntryService

    @Autowired
    private lateinit var accountService: AccountService

    @Test
    fun `should model realistic personal finance scenario with World account and opening balance`() {
        // ============================================================================
        // SETUP: Create all accounts needed for personal finance tracking
        // ============================================================================

        // 1. World Account (EQUITY) - Represents everything outside the tracking system
        val worldAccount = accountService.createAccount(
            CreateAccountInput(
                name = "World (External)",
                accountType = AccountType.EQUITY,
                userGroupId = 1L
            )
        )

        // 2. Asset Accounts - Things you own
        val savingsAccount = accountService.createAccount(
            CreateAccountInput(
                name = "Savings Account",
                accountType = AccountType.ASSET,
                userGroupId = 1L
            )
        )

        // 3. Income Accounts - Money coming in
        val salaryIncomeAccount = accountService.createAccount(
            CreateAccountInput(
                name = "Salary Income",
                accountType = AccountType.INCOME,
                userGroupId = 1L
            )
        )

        // 4. Expense Accounts - Money going out
        val rentExpenseAccount = accountService.createAccount(
            CreateAccountInput(
                name = "Rent Expense",
                accountType = AccountType.EXPENSE,
                userGroupId = 1L
            )
        )

        val groceriesExpenseAccount = accountService.createAccount(
            CreateAccountInput(
                name = "Groceries Expense",
                accountType = AccountType.EXPENSE,
                userGroupId = 1L
            )
        )

        val utilitiesExpenseAccount = accountService.createAccount(
            CreateAccountInput(
                name = "Utilities Expense",
                accountType = AccountType.EXPENSE,
                userGroupId = 1L
            )
        )

        // ============================================================================
        // TRANSACTION 1: Opening Balance
        // ============================================================================
        // Scenario: I'm starting to track my finances. I currently have $10,000 in savings.
        // This money came from "somewhere" (past earnings, gifts, etc.) - represented by World account.
        //
        // Accounting Treatment:
        // - World (EQUITY) is CREDITED → Increases equity (my net worth starts at $10,000)
        // - Savings (ASSET) is DEBITED → Increases asset (I have $10,000 in the bank)
        //
        // Accounting Equation: Assets ($10,000) = Liabilities ($0) + Equity ($10,000) ✅

        val openingBalance = journalEntryService.createJournalEntry(
            CreateJournalEntryInput(
                amount = Money(10000.0, Currency.USD),
                description = "Opening Balance",
                memo = "Initial balance when starting to track personal finances",
                transactionDate = Instant.now(),
                sourceAccountId = worldAccount.id!!,      // EQUITY - will be CREDITED
                destinationAccountId = savingsAccount.id!! // ASSET - will be DEBITED
            )
        )

        // Verify opening balance transaction
        assertThat(openingBalance.items).hasSize(2)
        val obWorldItem = openingBalance.items.find { it.accountId == worldAccount.id }
        val obSavingsItem = openingBalance.items.find { it.accountId == savingsAccount.id }

        assertThat(obWorldItem).isNotNull
        assertThat(obWorldItem?.direction).isEqualTo(Direction.CREDIT) // Equity increases
        assertThat(obWorldItem?.billingAmount?.amount).isEqualTo(10000.0)

        assertThat(obSavingsItem).isNotNull
        assertThat(obSavingsItem?.direction).isEqualTo(Direction.DEBIT) // Asset increases
        assertThat(obSavingsItem?.billingAmount?.amount).isEqualTo(10000.0)

        // Running balance: Savings = $10,000

        // ============================================================================
        // TRANSACTION 2: Receive Monthly Salary
        // ============================================================================
        // Scenario: I receive my monthly salary of $5,000 deposited into savings.
        //
        // Accounting Treatment:
        // - Salary Income (INCOME) is CREDITED → Increases income (I earned $5,000)
        // - Savings (ASSET) is DEBITED → Increases asset (bank balance goes up)
        //
        // Note: We use INCOME account, NOT World account, because this is regular income
        // that should appear on the income statement.
        //
        // Accounting Equation: Assets ($15,000) = Liabilities ($0) + Equity ($10,000) + Income ($5,000) ✅

        val salaryReceipt = journalEntryService.createJournalEntry(
            CreateJournalEntryInput(
                amount = Money(5000.0, Currency.USD),
                description = "Monthly Salary",
                memo = "January 2025 salary deposit",
                transactionDate = Instant.now(),
                sourceAccountId = salaryIncomeAccount.id!!,  // INCOME - will be CREDITED
                destinationAccountId = savingsAccount.id!!    // ASSET - will be DEBITED
            )
        )

        // Verify salary transaction
        assertThat(salaryReceipt.items).hasSize(2)
        val salaryIncomeItem = salaryReceipt.items.find { it.accountId == salaryIncomeAccount.id }
        val salarySavingsItem = salaryReceipt.items.find { it.accountId == savingsAccount.id }

        assertThat(salaryIncomeItem?.direction).isEqualTo(Direction.CREDIT) // Income increases
        assertThat(salaryIncomeItem?.billingAmount?.amount).isEqualTo(5000.0)
        assertThat(salarySavingsItem?.direction).isEqualTo(Direction.DEBIT) // Asset increases
        assertThat(salarySavingsItem?.billingAmount?.amount).isEqualTo(5000.0)

        // Running balance: Savings = $10,000 + $5,000 = $15,000

        // ============================================================================
        // TRANSACTION 3: Pay Rent
        // ============================================================================
        // Scenario: I pay $1,500 for monthly rent from my savings account.
        //
        // Accounting Treatment:
        // - Savings (ASSET) is CREDITED → Decreases asset (money leaves bank account)
        // - Rent Expense (EXPENSE) is DEBITED → Increases expense (I spent money on rent)
        //
        // Accounting Equation: Assets ($13,500) = Liabilities ($0) + Equity ($10,000) + Income ($5,000) - Expenses ($1,500) ✅

        val rentPayment = journalEntryService.createJournalEntry(
            CreateJournalEntryInput(
                amount = Money(1500.0, Currency.USD),
                description = "Monthly Rent Payment",
                memo = "January 2025 rent",
                transactionDate = Instant.now(),
                sourceAccountId = savingsAccount.id!!,      // ASSET - will be CREDITED
                destinationAccountId = rentExpenseAccount.id!! // EXPENSE - will be DEBITED
            )
        )

        // Verify rent payment
        assertThat(rentPayment.items).hasSize(2)
        val rentSavingsItem = rentPayment.items.find { it.accountId == savingsAccount.id }
        val rentExpenseItem = rentPayment.items.find { it.accountId == rentExpenseAccount.id }

        assertThat(rentSavingsItem?.direction).isEqualTo(Direction.CREDIT) // Asset decreases
        assertThat(rentSavingsItem?.billingAmount?.amount).isEqualTo(1500.0)
        assertThat(rentExpenseItem?.direction).isEqualTo(Direction.DEBIT) // Expense increases
        assertThat(rentExpenseItem?.billingAmount?.amount).isEqualTo(1500.0)

        // Running balance: Savings = $15,000 - $1,500 = $13,500

        // ============================================================================
        // TRANSACTION 4: Buy Groceries
        // ============================================================================
        // Scenario: I spend $300 on groceries from my savings account.
        //
        // Accounting Treatment:
        // - Savings (ASSET) is CREDITED → Decreases asset
        // - Groceries Expense (EXPENSE) is DEBITED → Increases expense

        val groceriesPurchase = journalEntryService.createJournalEntry(
            CreateJournalEntryInput(
                amount = Money(300.0, Currency.USD),
                description = "Groceries Purchase",
                memo = "Weekly grocery shopping",
                transactionDate = Instant.now(),
                sourceAccountId = savingsAccount.id!!,
                destinationAccountId = groceriesExpenseAccount.id!!
            )
        )

        // Verify groceries transaction
        assertThat(groceriesPurchase.items).hasSize(2)
        val groceriesSavingsItem = groceriesPurchase.items.find { it.accountId == savingsAccount.id }
        val groceriesExpenseItem = groceriesPurchase.items.find { it.accountId == groceriesExpenseAccount.id }

        assertThat(groceriesSavingsItem?.direction).isEqualTo(Direction.CREDIT) // Asset decreases
        assertThat(groceriesSavingsItem?.billingAmount?.amount).isEqualTo(300.0)
        assertThat(groceriesExpenseItem?.direction).isEqualTo(Direction.DEBIT) // Expense increases
        assertThat(groceriesExpenseItem?.billingAmount?.amount).isEqualTo(300.0)

        // Running balance: Savings = $13,500 - $300 = $13,200

        // ============================================================================
        // TRANSACTION 5: Pay Utilities
        // ============================================================================
        // Scenario: I pay $150 for utilities (electricity, water, internet) from savings.
        //
        // Accounting Treatment:
        // - Savings (ASSET) is CREDITED → Decreases asset
        // - Utilities Expense (EXPENSE) is DEBITED → Increases expense

        val utilitiesPayment = journalEntryService.createJournalEntry(
            CreateJournalEntryInput(
                amount = Money(150.0, Currency.USD),
                description = "Utilities Payment",
                memo = "Electricity, water, and internet",
                transactionDate = Instant.now(),
                sourceAccountId = savingsAccount.id!!,
                destinationAccountId = utilitiesExpenseAccount.id!!
            )
        )

        // Verify utilities transaction
        assertThat(utilitiesPayment.items).hasSize(2)
        val utilitiesSavingsItem = utilitiesPayment.items.find { it.accountId == savingsAccount.id }
        val utilitiesExpenseItem = utilitiesPayment.items.find { it.accountId == utilitiesExpenseAccount.id }

        assertThat(utilitiesSavingsItem?.direction).isEqualTo(Direction.CREDIT) // Asset decreases
        assertThat(utilitiesSavingsItem?.billingAmount?.amount).isEqualTo(150.0)
        assertThat(utilitiesExpenseItem?.direction).isEqualTo(Direction.DEBIT) // Expense increases
        assertThat(utilitiesExpenseItem?.billingAmount?.amount).isEqualTo(150.0)

        // Running balance: Savings = $13,200 - $150 = $13,050

        // ============================================================================
        // TRANSACTION 6: Transfer to External Account (Using World Account)
        // ============================================================================
        // Scenario: I transfer $1,000 to a friend (external account not tracked in system).
        //
        // Accounting Treatment:
        // - Savings (ASSET) is CREDITED → Decreases asset (money leaves my account)
        // - World (EQUITY) is DEBITED → Decreases equity (my net worth decreases)
        //
        // Note: We use World account here because the friend's account is not tracked
        // in our system. This is different from expenses - it's a transfer of value
        // outside the system.

        val externalTransfer = journalEntryService.createJournalEntry(
            CreateJournalEntryInput(
                amount = Money(1000.0, Currency.USD),
                description = "Transfer to Friend",
                memo = "Loan to friend - external account",
                transactionDate = Instant.now(),
                sourceAccountId = savingsAccount.id!!,  // ASSET - will be CREDITED
                destinationAccountId = worldAccount.id!! // EQUITY - will be DEBITED
            )
        )

        // Verify external transfer
        assertThat(externalTransfer.items).hasSize(2)
        val transferSavingsItem = externalTransfer.items.find { it.accountId == savingsAccount.id }
        val transferWorldItem = externalTransfer.items.find { it.accountId == worldAccount.id }

        assertThat(transferSavingsItem?.direction).isEqualTo(Direction.CREDIT) // Asset decreases
        assertThat(transferSavingsItem?.billingAmount?.amount).isEqualTo(1000.0)
        assertThat(transferWorldItem?.direction).isEqualTo(Direction.DEBIT) // Equity decreases
        assertThat(transferWorldItem?.billingAmount?.amount).isEqualTo(1000.0)

        // Running balance: Savings = $13,050 - $1,000 = $12,050

        // ============================================================================
        // FINAL VERIFICATIONS
        // ============================================================================

        // 1. Verify all transactions can be fetched back
        val fetchedOpeningBalance = journalEntryService.getJournalEntryById(openingBalance.id!!)
        val fetchedSalary = journalEntryService.getJournalEntryById(salaryReceipt.id!!)
        val fetchedRent = journalEntryService.getJournalEntryById(rentPayment.id!!)
        val fetchedGroceries = journalEntryService.getJournalEntryById(groceriesPurchase.id!!)
        val fetchedUtilities = journalEntryService.getJournalEntryById(utilitiesPayment.id!!)
        val fetchedTransfer = journalEntryService.getJournalEntryById(externalTransfer.id!!)

        assertThat(fetchedOpeningBalance.description).isEqualTo("Opening Balance")
        assertThat(fetchedSalary.description).isEqualTo("Monthly Salary")
        assertThat(fetchedRent.description).isEqualTo("Monthly Rent Payment")
        assertThat(fetchedGroceries.description).isEqualTo("Groceries Purchase")
        assertThat(fetchedUtilities.description).isEqualTo("Utilities Payment")
        assertThat(fetchedTransfer.description).isEqualTo("Transfer to Friend")

        // 2. Verify the accounting equation: Total Debits = Total Credits
        val allTransactions = listOf(
            openingBalance,
            salaryReceipt,
            rentPayment,
            groceriesPurchase,
            utilitiesPayment,
            externalTransfer
        )

        val totalDebits = allTransactions.flatMap { it.items }
            .filter { it.direction == Direction.DEBIT }
            .sumOf { it.billingAmount.amount }

        val totalCredits = allTransactions.flatMap { it.items }
            .filter { it.direction == Direction.CREDIT }
            .sumOf { it.billingAmount.amount }

        assertThat(totalDebits).isEqualTo(totalCredits)
        // Total: 10,000 + 5,000 + 1,500 + 300 + 150 + 1,000 = 17,950
        assertThat(totalDebits).isEqualTo(17950.0)



        // 3. Verify Savings account balance calculation
        // Opening: +$10,000
        // Salary:  +$5,000
        // Rent:    -$1,500
        // Groceries: -$300
        // Utilities: -$150
        // Transfer: -$1,000
        // Final: $12,050

        val savingsDebits = allTransactions.flatMap { it.items }
            .filter { it.accountId == savingsAccount.id && it.direction == Direction.DEBIT }
            .sumOf { it.billingAmount.amount }

        val savingsCredits = allTransactions.flatMap { it.items }
            .filter { it.accountId == savingsAccount.id && it.direction == Direction.CREDIT }
            .sumOf { it.billingAmount.amount }

        val savingsBalance = savingsDebits - savingsCredits
        assertThat(savingsBalance).isEqualTo(12050.0)

        // 4. Verify Income Statement components
        // Total Income: $5,000 (salary)
        val totalIncome = allTransactions.flatMap { it.items }
            .filter { it.accountId == salaryIncomeAccount.id && it.direction == Direction.CREDIT }
            .sumOf { it.billingAmount.amount }
        assertThat(totalIncome).isEqualTo(5000.0)

        // Total Expenses: $1,500 (rent) + $300 (groceries) + $150 (utilities) = $1,950
        val totalExpenses = allTransactions.flatMap { it.items }
            .filter { it.direction == Direction.DEBIT }
            .filter {
                it.accountId == rentExpenseAccount.id ||
                it.accountId == groceriesExpenseAccount.id ||
                it.accountId == utilitiesExpenseAccount.id
            }
            .sumOf { it.billingAmount.amount }
        assertThat(totalExpenses).isEqualTo(1950.0)

        // Net Income: $5,000 - $1,950 = $3,050
        val netIncome = totalIncome - totalExpenses
        assertThat(netIncome).isEqualTo(3050.0)

        // 5. Verify Balance Sheet components
        // Assets: $12,050 (savings account balance)
        assertThat(savingsBalance).isEqualTo(12050.0)

        // Equity changes through World account:
        // Opening: +$10,000 (CREDIT)
        // Transfer: -$1,000 (DEBIT)
        // Net Equity from World: $9,000
        val worldCredits = allTransactions.flatMap { it.items }
            .filter { it.accountId == worldAccount.id && it.direction == Direction.CREDIT }
            .sumOf { it.billingAmount.amount }
        val worldDebits = allTransactions.flatMap { it.items }
            .filter { it.accountId == worldAccount.id && it.direction == Direction.DEBIT }
            .sumOf { it.billingAmount.amount }
        val worldEquity = worldCredits - worldDebits
        assertThat(worldEquity).isEqualTo(9000.0)

        // 6. Verify Accounting Equation: Assets = Liabilities + Equity + (Income - Expenses)
        // $12,050 = $0 + $9,000 + ($5,000 - $1,950)
        // $12,050 = $9,000 + $3,050
        // $12,050 = $12,050 ✅
        assertThat(savingsBalance).isEqualTo(worldEquity + netIncome)

        // ============================================================================
        // SUMMARY OF PERSONAL FINANCE SCENARIO
        // ============================================================================
        // Starting Position:
        //   - Opening Balance: $10,000 (from World account)
        //
        // Income:
        //   - Salary: $5,000
        //
        // Expenses:
        //   - Rent: $1,500
        //   - Groceries: $300
        //   - Utilities: $150
        //   - Total Expenses: $1,950
        //
        // External Transfers:
        //   - Transfer to Friend: $1,000
        //
        // Final Position:
        //   - Savings Balance: $12,050
        //   - Net Income: $3,050
        //   - Net Worth: $12,050 (Assets) = $9,000 (Equity) + $3,050 (Net Income)
        //
        // All accounting equations balanced! ✅
        // ============================================================================
    }

}
