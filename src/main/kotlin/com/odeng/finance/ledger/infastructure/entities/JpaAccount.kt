package com.odeng.finance.ledger.infastructure.entities

import com.odeng.finance.ledger.domain.models.AccountType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Audited
@EntityListeners(AuditingEntityListener::class)
@SequenceGenerator(name = "account_seq_generator", sequenceName = "ledger.account_seq", allocationSize = 1)
@Table(schema = "ledger", name = "account")
class JpaAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq_generator")
    @Column(nullable = false)
    val id: Long,

    @Column(name = "user_group_id", nullable = false)
    val userGroupId: Long,

    @Column(name = "name", nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    val accountType: AccountType,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean,

    @CreatedDate
    @Column(name = "created_on", updatable = false)
    val createdOn: Instant? = null,

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    val createdBy: Long? = null,

    @LastModifiedDate
    @Column(name = "updated_on")
    var updatedOn: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_by")
    var updatedBy: Long? = null
) {
}