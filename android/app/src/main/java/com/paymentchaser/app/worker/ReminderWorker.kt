package com.paymentchaser.app.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paymentchaser.app.MainActivity
import com.paymentchaser.app.PaymentChaserApp
import com.paymentchaser.app.R

class ReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val clientName = inputData.getString(KEY_CLIENT_NAME) ?: "Client"
        val invoiceNumber = inputData.getString(KEY_INVOICE_NUMBER) ?: ""
        val amount = inputData.getString(KEY_AMOUNT) ?: ""
        val paymentId = inputData.getString(KEY_PAYMENT_ID) ?: ""

        sendNotification(clientName, invoiceNumber, amount, paymentId)
        return Result.success()
    }

    private fun sendNotification(
        clientName: String,
        invoiceNumber: String,
        amount: String,
        paymentId: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("EXTRA_PAYMENT_ID", paymentId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            paymentId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, PaymentChaserApp.CHANNEL_REMINDERS_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Payment Follow-Up Due: $clientName")
            .setContentText("Invoice #$invoiceNumber for $amount is ready for scheduled follow-up.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(paymentId.hashCode(), notification)
    }

    companion object {
        const val KEY_CLIENT_NAME = "key_client_name"
        const val KEY_INVOICE_NUMBER = "key_invoice_number"
        const val KEY_AMOUNT = "key_amount"
        const val KEY_PAYMENT_ID = "key_payment_id"
    }
}
