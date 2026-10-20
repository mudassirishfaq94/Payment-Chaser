package com.paymentchaser.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.paymentchaser.app.data.model.ClientEntity
import com.paymentchaser.app.data.model.PaymentEntity
import com.paymentchaser.app.data.model.ReminderLogEntity

@Database(
    entities = [
        ClientEntity::class,
        PaymentEntity::class,
        ReminderLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PaymentDatabase : RoomDatabase() {
    abstract fun paymentDao(): PaymentDao
    abstract fun clientDao(): ClientDao
    abstract fun reminderLogDao(): ReminderLogDao

    companion object {
        const val DATABASE_NAME = "payment_chaser.db"
    }
}
