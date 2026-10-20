package com.paymentchaser.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paymentchaser.app.data.model.ReminderLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderLogDao {

    @Query("SELECT * FROM reminder_logs WHERE paymentId = :paymentId ORDER BY timestamp DESC")
    fun getLogsForPayment(paymentId: String): Flow<List<ReminderLogEntity>>

    @Query("SELECT * FROM reminder_logs ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentLogs(limit: Int = 20): Flow<List<ReminderLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ReminderLogEntity)

    @Query("DELETE FROM reminder_logs WHERE paymentId = :paymentId")
    suspend fun deleteLogsForPayment(paymentId: String)
}
