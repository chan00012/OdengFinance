package com.odeng.finance.ledger.application

import com.odeng.finance.common.Money
import java.time.Instant

/**
 * Input for creating a journal entry in a double-entry accounting system.
 *
 * In double-entry accounting, every transaction affects exactly two accounts:
 * - One account is CREDITED
 * - One account is DEBITED
 *
 * The amounts must be equal to maintain the accounting equation: Assets = Liabilities + Equity
 *
 * ## Understanding Source and Destination:
 *
 * - **sourceAccountId**: The account that will be CREDITED in this transaction
 * - **destinationAccountId**: The account that will be DEBITED in this transaction
 *
 * ## How DEBIT and CREDIT affect different account types:
 *
 * | Account Type | DEBIT Effect | CREDIT Effect |
 * |--------------|--------------|---------------|
 * | ASSET        | Increase ↑   | Decrease ↓    |
 * | LIABILITY    | Decrease ↓   | Increase ↑    |
 * | EQUITY       | Decrease ↓   | Increase ↑    |
 * | INCOME       | Decrease ↓   | Increase ↑    |
 * | EXPENSE      | Increase ↑   | Decrease ↓    |
 *
 * ## Examples:
 *
 * ### Example 1: Pay $100 cash for office supplies
 * ```
 * sourceAccountId = cashAccountId (ASSET - will be CREDITED = decreases)
 * destinationAccountId = officeSuppliesAccountId (EXPENSE - will be DEBITED = increases)
 * Result: Cash decreases by $100, Office Supplies expense increases by $100
 * ```
 *
 * ### Example 2: Receive $5000 salary into bank account
 * ```
 * sourceAccountId = salaryIncomeAccountId (INCOME - will be CREDITED = increases)
 * destinationAccountId = bankAccountId (ASSET - will be DEBITED = increases)
 * Result: Salary income increases by $5000, Bank balance increases by $5000
 * ```
 *
 * ### Example 3: Transfer $1000 from checking to savings
 * ```
 * sourceAccountId = checkingAccountId (ASSET - will be CREDITED = decreases)
 * destinationAccountId = savingsAccountId (ASSET - will be DEBITED = increases)
 * Result: Checking decreases by $1000, Savings increases by $1000
 * ```
 *
 * ### Example 4: Pay down $500 on a loan
 * ```
 * sourceAccountId = cashAccountId (ASSET - will be CREDITED = decreases)
 * destinationAccountId = loanAccountId (LIABILITY - will be DEBITED = decreases)
 * Result: Cash decreases by $500, Loan liability decreases by $500
 * ```
 *
 * @property amount The monetary amount for this transaction
 * @property description A brief description of the transaction
 * @property memo Additional notes or details about the transaction
 * @property transactionDate The date when the transaction occurred
 * @property sourceAccountId The account to be CREDITED (see examples above)
 * @property destinationAccountId The account to be DEBITED (see examples above)
 */
data class CreateJournalEntryInput(
    val amount: Money,
    val description: String,
    val memo: String,
    val transactionDate: Instant,
    val sourceAccountId: Long,
    val destinationAccountId: Long
)
