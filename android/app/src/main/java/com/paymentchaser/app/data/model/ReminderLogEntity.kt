package com.paymentchaser.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminder_logs",
    foreignKeys = [
        ForeignKey(
            entity = PaymentEntity::class,
            parentColumns = ["id"],
            childColumns = ["paymentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["paymentId"]),
        Index(value = ["timestamp"])
    ]
)
data class ReminderLogEntity(
    @PrimaryKey
    val id: String,
    val paymentId: String,
    val clientName: String,
    val channel: String, // "WhatsApp", "Email", "Notification", "SMS"
    val actionType: String, // "MANUALLY_SHARED", "OPENED_FOR_SHARING", "LOCAL_NOTIFICATION_TRIGGERED"
    val messageSnippet: String,
    val timestamp: Long = System.currentTimeMillis()
)
