package com.paymentchaser.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val name: String,
    val company: String?,
    val email: String,
    val phone: String,
    val initials: String,
    val notes: String? = null,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
