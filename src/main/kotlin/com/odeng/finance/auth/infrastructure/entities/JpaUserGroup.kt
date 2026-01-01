package com.odeng.finance.auth.infrastructure.entities

import jakarta.persistence.*
import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant


@Entity
@Audited
@EntityListeners(AuditingEntityListener::class)
@SequenceGenerator(name = "user_group_seq_generator", sequenceName = "authz.user_group_seq", allocationSize = 1)
@Table(schema = "authz", name = "user_group")
class JpaUserGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_group_seq_generator")
    val id: Long? = null,

    @CreatedDate
    @Column(name = "created_on", updatable = false)
    var createdOn: Instant? = null,

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: Long? = null,

    @LastModifiedDate
    @Column(name = "updated_on")
    var updatedOn: Instant? = null,

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: Long? = null
) {
}