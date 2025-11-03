package com.odeng.finance.ledger.interfaces.rest

import com.odeng.finance.ledger.interfaces.rest.api.JournalEntriesApi
import com.odeng.finance.ledger.interfaces.rest.api.model.*
import com.odeng.finance.ledger.application.CreateJournalEntryInput
import com.odeng.finance.ledger.application.JournalEntryService
import com.odeng.finance.common.Currency as DomainCurrency
import com.odeng.finance.common.Money as DomainMoney
import com.odeng.finance.ledger.domain.Direction as DomainDirection
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

/**
 * REST controller implementation for Journal Entries API.
 * 
 * This controller implements the JournalEntriesApi interface generated from the OpenAPI specification.
 * It acts as an adapter between the REST API layer and the application service layer,
 * following the Hexagonal Architecture pattern.
 */
@RestController
class JournalEntriesController(
    private val journalEntryService: JournalEntryService
) : JournalEntriesApi {

    private companion object {
        val logger = KotlinLogging.logger {}
    }

    /**
     * Creates a new journal entry.
     * 
     * Maps the API request to the domain input and converts the domain response back to API response.
     */
    override fun createJournalEntry(
        createJournalEntryRequest: CreateJournalEntryRequest
    ): ResponseEntity<JournalEntryResponse> {
        logger.info { "Creating journal entry: ${createJournalEntryRequest.description}" }

        // Map API request to domain input
        val input = CreateJournalEntryInput(
            amount = DomainMoney(
                amount = createJournalEntryRequest.amount.amount,
                currency = mapCurrency(createJournalEntryRequest.amount.currency)
            ),
            description = createJournalEntryRequest.description,
            memo = createJournalEntryRequest.memo,
            transactionDate = createJournalEntryRequest.transactionDate,
            sourceAccountId = createJournalEntryRequest.sourceAccountId,
            destinationAccountId = createJournalEntryRequest.destinationAccountId
        )
        
        // Call domain service
        val journalEntry = journalEntryService.createJournalEntry(input)
        
        // Map domain response to API response
        val response = JournalEntryResponse(
            id = journalEntry.id!!,
            description = journalEntry.description,
            memo = journalEntry.memo,
            transactionDate = journalEntry.transactionDate,
            entryItems = journalEntry.items.map { item ->
                EntryItemResponse(
                    id = item.id!!,
                    accountId = item.accountId,
                    journalId = item.journalId,
                    billingAmount = item.billingAmount.amount,
                    currency = mapCurrencyToApi(item.billingAmount.currency),
                    direction = mapDirectionToApi(item.direction)
                )
            }
        )
        
        logger.info { "Journal entry created successfully: ${response.id}" }
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    /**
     * Retrieves a journal entry by its ID.
     */
    override fun getJournalEntryById(journalEntryId: Long): ResponseEntity<JournalEntryResponse> {
        logger.info { "Fetching journal entry with ID: $journalEntryId" }
        
        val journalEntry = try {
            journalEntryService.getJournalEntryById(journalEntryId)
        } catch (e: IllegalStateException) {
            logger.warn { "Journal entry not found: $journalEntryId" }
            return ResponseEntity.notFound().build()
        }
        
        val response = JournalEntryResponse(
            id = journalEntry.id!!,
            description = journalEntry.description,
            memo = journalEntry.memo,
            transactionDate = journalEntry.transactionDate,
            entryItems = journalEntry.items.map { item ->
                EntryItemResponse(
                    id = item.id!!,
                    accountId = item.accountId,
                    journalId = item.journalId,
                    billingAmount = item.billingAmount.amount,
                    currency = mapCurrencyToApi(item.billingAmount.currency),
                    direction = mapDirectionToApi(item.direction)
                )
            }
        )
        
        logger.info { "Journal entry retrieved successfully: ${response.id}" }
        return ResponseEntity.ok(response)
    }

    // ============================================================================
    // Mapping Functions: API Models <-> Domain Models
    // ============================================================================

    private fun mapCurrency(apiCurrency: Currency): DomainCurrency {
        return DomainCurrency.valueOf(apiCurrency.name)
    }

    private fun mapCurrencyToApi(domainCurrency: DomainCurrency): Currency {
        return Currency.valueOf(domainCurrency.name)
    }

    private fun mapDirectionToApi(domainDirection: DomainDirection): Direction {
        return when (domainDirection) {
            DomainDirection.DEBIT -> Direction.DEBIT
            DomainDirection.CREDIT -> Direction.CREDIT
        }
    }
}

