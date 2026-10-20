package com.paymentchaser.app.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.net.URLEncoder

object ShareIntentsHelper {

    /**
     * Opens WhatsApp with pre-formatted reminder text and client phone.
     * Note: This opens the external application. It does not verify delivery.
     */
    fun openWhatsAppChat(context: Context, phone: String, message: String): Boolean {
        return try {
            val cleanPhone = phone.replace(Regex("[^0-9]"), "")
            val encodedMessage = URLEncoder.encode(message, "UTF-8")
            val url = "https://api.whatsapp.com/send?phone=$cleanPhone&text=$encodedMessage"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show()
            false
        }
    }

    /**
     * Opens user's default email client with recipient, subject, and pre-composed body.
     */
    fun openEmailDraft(context: Context, email: String, subject: String, body: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            Toast.makeText(context, "No email client found on this device", Toast.LENGTH_SHORT).show()
            false
        }
    }

    /**
     * Opens native SMS messenger.
     */
    fun openSms(context: Context, phone: String, message: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:${phone.replace(Regex("[^0-9+]"), "")}")
                putExtra("sms_body", message)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to launch SMS app", Toast.LENGTH_SHORT).show()
            false
        }
    }

    /**
     * Copies text to system clipboard.
     */
    fun copyToClipboard(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied reminder to clipboard", Toast.LENGTH_SHORT).show()
    }
}
