package com.odeng.finance.auth.domain.model

enum class AccessType {
    OWNER,
    EDITOR,
    VIEWER;

    fun canRead(): Boolean = true

    fun canWrite(): Boolean = when (this) {
        OWNER, EDITOR -> true
        else -> false
    }

    fun canDelete(): Boolean = when (this) {
        OWNER -> true
        else -> false
    }

    fun canShare(): Boolean = when (this) {
        OWNER -> true
        else -> false
    }
}