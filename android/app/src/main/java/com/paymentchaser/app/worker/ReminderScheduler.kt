package com.paymentchaser.app.worker

import android.content.Context
import androidx.work.*
import com.paymentchaser.app.data.model.PaymentEntity
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    private fun getWorkTag(paymentId: String, offsetDays: Int): String {
        return "reminder_${paymentId}_offset_$offsetDays"
    }

    /**
     * Schedules multi-stage reminder local notifications:
     * - 3 days before due date
     * - On due date (0)
     * - 3 days overdue (+3)
     * - 7 days overdue (+7)
     */
    fun schedulePaymentReminders(context: Context, payment: PaymentEntity) {
        if (!payment.smartSequenceEnabled || payment.status.name == "PAID") {
            cancelPaymentReminders(context, payment.id)
            return
        }

        val workManager = WorkManager.getInstance(context)
        val offsets = listOf(-3, 0, 3, 7)
        val now = System.currentTimeMillis()

        for (offsetDays in offsets) {
            val targetTime = payment.dueDateMillis + (offsetDays * 86400000L)
            val delayMillis = targetTime - now

            // Only schedule if the trigger time is in the future
            if (delayMillis > 0) {
                val inputData = Data.Builder()
                    .putString(ReminderWorker.KEY_PAYMENT_ID, payment.id)
                    .putString(ReminderWorker.KEY_CLIENT_NAME, payment.clientName)
                    .putString(ReminderWorker.KEY_INVOICE_NUMBER, payment.invoiceNumber)
                    .putString(ReminderWorker.KEY_AMOUNT, "$${payment.remainingDecimal}")
                    .build()

                val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                    .setInputData(inputData)
                    .addTag("payment_${payment.id}")
                    .addTag(getWorkTag(payment.id, offsetDays))
                    .build()

                workManager.enqueueUniqueWork(
                    getWorkTag(payment.id, offsetDays),
                    ExistingWorkPolicy.REPLACE,
                    workRequest
                )
            }
        }
    }

    /**
     * Cancels all scheduled reminder work when payment is marked paid
     */
    fun cancelPaymentReminders(context: Context, paymentId: String) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag("payment_$paymentId")
    }

    /**
     * Reschedules when due date is updated
     */
    fun reschedulePaymentReminders(context: Context, payment: PaymentEntity) {
        cancelPaymentReminders(context, payment.id)
        schedulePaymentReminders(context, payment)
    }
}
