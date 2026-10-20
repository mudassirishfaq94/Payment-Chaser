package com.paymentchaser.app.data.local

import androidx.room.TypeConverter
import com.paymentchaser.app.data.model.PaymentStatus

class Converters {

    @TypeConverter
    fun fromPaymentStatus(status: PaymentStatus): String {
        return status.name
    }

    @TypeConverter
    fun toPaymentStatus(value: String): PaymentStatus {
        return try {
            PaymentStatus.valueOf(value)
        } catch (e: IllegalArgumentException) {
            PaymentStatus.PENDING
        }
    }
}
