package com.paymentchaser.app.data.repository

import android.content.Context
import com.paymentchaser.app.data.local.ClientDao
import com.paymentchaser.app.data.local.PaymentDao
import com.paymentchaser.app.data.local.ReminderLogDao
import com.paymentchaser.app.data.model.ClientEntity
import com.paymentchaser.app.data.model.PaymentEntity
import com.paymentchaser.app.data.model.PaymentStatus
import com.paymentchaser.app.data.model.ReminderLogEntity
import com.paymentchaser.app.worker.ReminderScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val paymentDao: PaymentDao,
    private val clientDao: ClientDao,
    private val reminderLogDao: ReminderLogDao,
    private val syncRepository: SyncRepository
) : PaymentRepository {

    override fun getAllPayments(userId: String): Flow<List<PaymentEntity>> {
        return paymentDao.getAllPayments(userId)
    }

    override fun getPaymentsByStatus(userId: String, status: PaymentStatus): Flow<List<PaymentEntity>> {
        return paymentDao.getPaymentsByStatus(userId, status)
    }

    override fun observePayment(id: String): Flow<PaymentEntity?> {
        return paymentDao.observePaymentById(id)
    }

    override fun searchPayments(userId: String, query: String): Flow<List<PaymentEntity>> {
        return paymentDao.searchPayments(userId, query)
    }

    override fun getClients(userId: String): Flow<List<ClientEntity>> {
        return clientDao.getAllActiveClients(userId)
    }

    override fun observeTotalOutstandingCents(userId: String): Flow<Long?> {
        return paymentDao.observeTotalOutstandingCents(userId)
    }

    override fun observeTotalOverdueCents(userId: String): Flow<Long?> {
        return paymentDao.observeTotalOverdueCents(userId, System.currentTimeMillis())
    }

    override fun observeTotalCollectedCents(userId: String): Flow<Long?> {
        return paymentDao.observeTotalCollectedCents(userId)
    }

    override fun getReminderLogs(paymentId: String): Flow<List<ReminderLogEntity>> {
        return reminderLogDao.getLogsForPayment(paymentId)
    }

    override suspend fun insertPayment(payment: PaymentEntity) {
        paymentDao.insertPayment(payment)
        // Automatically schedule local reminder notifications via WorkManager
        ReminderScheduler.schedulePaymentReminders(context, payment)
        // Sync to cloud if available
        syncRepository.uploadPayment(payment)
    }

    override suspend fun updatePayment(payment: PaymentEntity) {
        paymentDao.updatePayment(payment)
        // Reschedule reminders on updated due date or active status
        ReminderScheduler.reschedulePaymentReminders(context, payment)
        syncRepository.uploadPayment(payment)
    }

    override suspend fun recordPartialPayment(id: String, centsToAdd: Long) {
        paymentDao.recordPartialPayment(id, centsToAdd)
        val updated = paymentDao.getPaymentById(id)
        if (updated != null) {
            if (updated.status == PaymentStatus.PAID) {
                ReminderScheduler.cancelPaymentReminders(context, id)
            }
            syncRepository.uploadPayment(updated)
        }
    }

    override suspend fun markPaymentAsPaid(id: String) {
        paymentDao.markPaymentAsPaid(id)
        // Immediately cancel pending reminders when marked paid (Rule 10)
        ReminderScheduler.cancelPaymentReminders(context, id)
        val updated = paymentDao.getPaymentById(id)
        if (updated != null) {
            syncRepository.uploadPayment(updated)
        }
    }

    override suspend fun deletePayment(payment: PaymentEntity) {
        ReminderScheduler.cancelPaymentReminders(context, payment.id)
        paymentDao.deletePayment(payment)
    }

    override suspend fun insertClient(client: ClientEntity) {
        clientDao.insertClient(client)
        syncRepository.uploadClient(client)
    }

    override suspend fun updateClient(client: ClientEntity) {
        clientDao.updateClient(client)
        syncRepository.uploadClient(client)
    }

    override suspend fun archiveClient(client: ClientEntity): Result<Unit> {
        val activeDebts = paymentDao.getActivePaymentsCountForClient(client.id)
        return if (activeDebts > 0) {
            Result.failure(IllegalStateException("Cannot archive client with $activeDebts active payment(s). Settle or cancel them first."))
        } else {
            clientDao.archiveClient(client.id)
            val updated = client.copy(isArchived = true)
            syncRepository.uploadClient(updated)
            Result.success(Unit)
        }
    }

    override suspend fun logReminderAction(log: ReminderLogEntity) {
        reminderLogDao.insertLog(log)
    }

    override suspend fun syncWithCloud(userId: String): Result<Unit> {
        return syncRepository.syncAll(userId)
    }
}
