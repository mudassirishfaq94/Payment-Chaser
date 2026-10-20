package com.paymentchaser.app.data.repository

import com.paymentchaser.app.data.model.ClientEntity
import com.paymentchaser.app.data.model.PaymentEntity
import com.paymentchaser.app.data.model.PaymentStatus
import com.paymentchaser.app.data.model.ReminderLogEntity
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    fun getAllPayments(userId: String): Flow<List<PaymentEntity>>
    fun getPaymentsByStatus(userId: String, status: PaymentStatus): Flow<List<PaymentEntity>>
    fun observePayment(id: String): Flow<PaymentEntity?>
    fun searchPayments(userId: String, query: String): Flow<List<PaymentEntity>>
    fun getClients(userId: String): Flow<List<ClientEntity>>
    fun observeTotalOutstandingCents(userId: String): Flow<Long?>
    fun observeTotalOverdueCents(userId: String): Flow<Long?>
    fun observeTotalCollectedCents(userId: String): Flow<Long?>
    fun getReminderLogs(paymentId: String): Flow<List<ReminderLogEntity>>

    suspend fun insertPayment(payment: PaymentEntity)
    suspend fun updatePayment(payment: PaymentEntity)
    suspend fun recordPartialPayment(id: String, centsToAdd: Long)
    suspend fun markPaymentAsPaid(id: String)
    suspend fun deletePayment(payment: PaymentEntity)
    suspend fun insertClient(client: ClientEntity)
    suspend fun updateClient(client: ClientEntity)
    suspend fun archiveClient(client: ClientEntity): Result<Unit>
    suspend fun logReminderAction(log: ReminderLogEntity)
    suspend fun syncWithCloud(userId: String): Result<Unit>
}
