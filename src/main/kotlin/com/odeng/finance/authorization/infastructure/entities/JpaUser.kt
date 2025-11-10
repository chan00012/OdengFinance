package com.odeng.finance.authorization.infastructure.entities

import com.odeng.finance.authorization.domain.model.User
import com.odeng.finance.authorization.domain.model.UserStatus
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
@SequenceGenerator(name = "user_seq_generator", sequenceName = "authz.user_seq", allocationSize = 1)
@Table(schema = "authz", name = "user")
class JpaUser(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_generator")
    val id: Long? = null,

    @Column(name = "username", nullable = false, updatable = false)
    val username: String,

    @Column(name = "email", nullable = false, updatable = false)
    val email: String,

    @Column(name = "hash_password", nullable = false)
    val hashPassword: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: UserStatus,

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
        fun JpaUser.toDomain(): User {
            return User(
                id = id,
                username = username,
                email = email,
                hashPassword = hashPassword,
                status = status
            )

        }
    }
}