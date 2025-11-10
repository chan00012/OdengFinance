package com.odeng.finance.ledger.application

import com.odeng.finance.ledger.domain.AccountRepository
import com.odeng.finance.ledger.domain.model.Direction
import com.odeng.finance.ledger.domain.model.EntryItem
import com.odeng.finance.ledger.domain.EntryItemRepository
import com.odeng.finance.ledger.domain.model.JournalEntry
import com.odeng.finance.ledger.domain.JournalEntryRepository
import org.springframework.stereotype.Service

@Service
class JournalEntryService(
    private val journalEntryRepository: JournalEntryRepository,
    private val accountRepository: AccountRepository,
    private val entryItemRepository: EntryItemRepository
) {

    /**
     * Creates a journal entry following double-entry accounting principles.
     *
     * This method creates a journal entry with exactly two entry items:
     * - One CREDIT entry for the source account
     * - One DEBIT entry for the destination account
     *
     * The method automatically applies the correct DEBIT/CREDIT based on the semantics:
     * - Source account is always CREDITED
     * - Destination account is always DEBITED
     *
     * The accounting effect (increase/decrease) depends on the account type.
     * See [CreateJournalEntryInput] documentation for detailed examples.
     *
     * ## Valid Transaction Types:
     * This method supports ALL valid accounting transactions, including:
     * - Same account type transactions (e.g., ASSET to ASSET for bank transfers)
     * - Cross account type transactions (e.g., ASSET to EXPENSE for payments)
     * - Income transactions (e.g., INCOME to ASSET for salary deposits)
     * - Liability transactions (e.g., ASSET to LIABILITY for loan payments)
     *
     * @param input The journal entry input containing accounts and transaction details
     * @return The created journal entry with two entry items
     * @throws NoSuchElementException if either account ID doesn't exist
     */
    fun createJournalEntry(input: CreateJournalEntryInput): JournalEntry {
        val journalEntry = journalEntryRepository.createDraft(
            description = input.description,
            memo = input.memo,
            transactionDate = input.transactionDate
        )

        // Validate that both accounts exist (will throw if not found)
        val sourceAccount = accountRepository.getById(input.sourceAccountId)
        val destinationAccount = accountRepository.getById(input.destinationAccountId)

        val sourceEntryItem = entryItemRepository.create(
            EntryItem(
                journalId = journalEntry.id!!,
                accountId = sourceAccount.id!!,
                billingAmount = input.amount,
                direction = Direction.CREDIT
            )
        )

        val destinationEntryItem = entryItemRepository.create(
            EntryItem(
                journalId = journalEntry.id!!,
                accountId = destinationAccount.id!!,
                billingAmount = input.amount,
                direction = Direction.DEBIT
            )
        )

        val journalEntryWithItems = journalEntry
            .addItem(sourceEntryItem)
            .addItem(destinationEntryItem)

        return journalEntryWithItems
    }

    fun getJournalEntryById(id: Long): JournalEntry {
        val journalEntry = journalEntryRepository.getById(id)
        val entryItems = entryItemRepository.getByJournalId(id)

        val journalEntryWithItems = entryItems.fold(journalEntry) { entry, item ->
            entry.addItem(item)
        }

        return journalEntryWithItems
    }
}