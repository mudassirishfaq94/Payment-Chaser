package com.paymentchaser.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.math.RoundingMode

@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["clientId"]),
        Index(value = ["userId"]),
        Index(value = ["dueDateMillis"]),
        Index(value = ["status"])
    ]
)
data class PaymentEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val clientId: String,
    val clientName: String,
    val clientEmail: String,
    val clientPhone: String,
    val clientInitials: String,
    val invoiceNumber: String,
    val scopeDescription: String,
    val amountCents: Long,          // Exact integer cents for decimal-safety (never Float/Double)
    val amountPaidCents: Long = 0L,  // Exact integer cents
    val currency: String = "USD",
    val issueDateMillis: Long,
    val dueDateMillis: Long,
    val status: PaymentStatus,
    val smartSequenceEnabled: Boolean = true,
    val channelEmail: Boolean = true,
    val channelWhatsApp: Boolean = true,
    val channelSms: Boolean = false,
    val tone: String = "friendly",
    val lastReminderTimestamp: Long? = null,
    val attachmentUri: String? = null,
    val isDraft: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    val remainingCents: Long
        get() = maxOf(0L, amountCents - amountPaidCents)

    val amountDecimal: BigDecimal
        get() = BigDecimal(amountCents).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)

    val amountPaidDecimal: BigDecimal
        get() = BigDecimal(amountPaidCents).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)

    val remainingDecimal: BigDecimal
        get() = BigDecimal(remainingCents).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)

    fun isOverdue(currentTimestamp: Long = System.currentTimeMillis()): Boolean {
        return status != PaymentStatus.PAID && dueDateMillis < currentTimestamp
    }
}
