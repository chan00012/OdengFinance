package com.odeng.finance.auth.infrastructure.entities

import com.odeng.finance.auth.domain.model.AccessType
import com.odeng.finance.auth.domain.model.UserRole
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
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Audited
@EntityListeners(AuditingEntityListener::class)
@SequenceGenerator(name = "user_role_seq_generator", sequenceName = "authz.user_role_seq", allocationSize = 1)
@Table(schema = "authz", name = "user_role")
class JpaUserRole(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_seq_generator")
    val id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "user_group_id", nullable = false)
    val userGroupId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type", nullable = false)
    val accessType: AccessType,

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
    companion object {
        fun JpaUserRole.toDomain(): UserRole {
            return UserRole(
                userId = userId,
                access = accessType
            )
        }
    }
}