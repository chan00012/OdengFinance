package com.odeng.finance.ledger.application

import com.odeng.finance.ledger.domain.AccountRepository
import com.odeng.finance.ledger.domain.Direction
import com.odeng.finance.ledger.domain.EntryItem
import com.odeng.finance.ledger.domain.EntryItemRepository
import com.odeng.finance.ledger.domain.JournalEntry
import com.odeng.finance.ledger.domain.JournalEntryRepository
import org.springframework.stereotype.Service

@Service
class JournalEntryService(
    private val journalEntryRepository: JournalEntryRepository,
    private val accountRepository: AccountRepository,
    private val entryItemRepository: EntryItemRepository
) {

    fun createJournalEntry(input: CreateJournalEntryInput): JournalEntry {
        val draftJournalEntry = journalEntryRepository.createDraft(
            description = input.description,
            memo = input.memo,
            transactionDate = input.transactionDate
        )

        val sourceAccount = accountRepository.getById(input.sourceAccountId)
        val destinationAccount = accountRepository.getById(input.destinationAccountId)

        require(sourceAccount.accountType != destinationAccount.accountType) {
            "Source and destination accounts must be of different types"
        }

        val sourceEntryItem = entryItemRepository.create(
            EntryItem(
                journalId = draftJournalEntry.id!!,
                accountId = input.sourceAccountId,
                billingAmount = input.amount,
                direction = Direction.DEBIT
            )
        )

        val destinationEntryItem = entryItemRepository.create(
            EntryItem(
                journalId = draftJournalEntry.id!!,
                accountId = input.destinationAccountId,
                billingAmount = input.amount,
                direction = Direction.CREDIT
            )
        )


        draftJournalEntry.addItem(sourceEntryItem)
        draftJournalEntry.addItem(destinationEntryItem)

        return draftJournalEntry
    }
}