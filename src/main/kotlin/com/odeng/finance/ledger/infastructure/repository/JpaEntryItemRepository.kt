package com.odeng.finance.ledger.infastructure.repository

import com.odeng.finance.ledger.infastructure.entities.JpaEntryItem
import org.springframework.data.jpa.repository.JpaRepository

interface JpaEntryItemRepository : JpaRepository<JpaEntryItem, Long> {
}