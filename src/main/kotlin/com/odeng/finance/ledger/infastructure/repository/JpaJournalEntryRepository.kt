package com.odeng.finance.ledger.infastructure.repository

import com.odeng.finance.ledger.infastructure.entities.JpaJournalEntry
import org.springframework.data.jpa.repository.JpaRepository

interface JpaJournalEntryRepository : JpaRepository<JpaJournalEntry, Long> {
}