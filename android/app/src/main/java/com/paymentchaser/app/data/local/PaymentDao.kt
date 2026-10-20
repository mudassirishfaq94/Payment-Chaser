package com.paymentchaser.app.data.local

import androidx.room.*
import com.paymentchaser.app.data.model.PaymentEntity
import com.paymentchaser.app.data.model.PaymentStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {

    @Query("SELECT * FROM payments WHERE userId = :userId ORDER BY dueDateMillis ASC")
    fun getAllPayments(userId: String): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE userId = :userId AND status = :status ORDER BY dueDateMillis ASC")
    fun getPaymentsByStatus(userId: String, status: PaymentStatus): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE id = :id LIMIT 1")
    suspend fun getPaymentById(id: String): PaymentEntity?

    @Query("SELECT * FROM payments WHERE id = :id LIMIT 1")
    fun observePaymentById(id: String): Flow<PaymentEntity?>

    @Query("SELECT * FROM payments WHERE clientId = :clientId ORDER BY dueDateMillis DESC")
    fun getPaymentsForClient(clientId: String): Flow<List<PaymentEntity>>

    @Query("SELECT COUNT(*) FROM payments WHERE clientId = :clientId AND status != 'PAID'")
    suspend fun getActivePaymentsCountForClient(clientId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity)

    @Update
    suspend fun updatePayment(payment: PaymentEntity)

    @Query("UPDATE payments SET amountPaidCents = amountPaidCents + :centsToAdd, status = CASE WHEN (amountPaidCents + :centsToAdd) >= amountCents THEN 'PAID' ELSE 'PARTIALLY_PAID' END WHERE id = :id")
    suspend fun recordPartialPayment(id: String, centsToAdd: Long)

    @Query("UPDATE payments SET amountPaidCents = amountCents, status = 'PAID', smartSequenceEnabled = 0 WHERE id = :id")
    suspend fun markPaymentAsPaid(id: String)

    @Delete
    suspend fun deletePayment(payment: PaymentEntity)

    @Query("SELECT * FROM payments WHERE userId = :userId AND (clientName LIKE '%' || :query || '%' OR invoiceNumber LIKE '%' || :query || '%' OR scopeDescription LIKE '%' || :query || '%') ORDER BY dueDateMillis ASC")
    fun searchPayments(userId: String, query: String): Flow<List<PaymentEntity>>

    @Query("SELECT SUM(amountCents - amountPaidCents) FROM payments WHERE userId = :userId AND status != 'PAID'")
    fun observeTotalOutstandingCents(userId: String): Flow<Long?>

    @Query("SELECT SUM(amountCents - amountPaidCents) FROM payments WHERE userId = :userId AND (status = 'OVERDUE' OR (status != 'PAID' AND dueDateMillis < :nowMillis))")
    fun observeTotalOverdueCents(userId: String, nowMillis: Long): Flow<Long?>

    @Query("SELECT SUM(amountPaidCents) FROM payments WHERE userId = :userId")
    fun observeTotalCollectedCents(userId: String): Flow<Long?>
}
