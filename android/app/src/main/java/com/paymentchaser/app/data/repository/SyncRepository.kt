package com.paymentchaser.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.paymentchaser.app.data.local.ClientDao
import com.paymentchaser.app.data.local.PaymentDao
import com.paymentchaser.app.data.model.ClientEntity
import com.paymentchaser.app.data.model.PaymentEntity
import com.paymentchaser.app.data.model.PaymentStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface SyncRepository {
    suspend fun syncAll(userId: String): Result<Unit>
    suspend fun uploadPayment(payment: PaymentEntity): Result<Unit>
    suspend fun uploadClient(client: ClientEntity): Result<Unit>
}

@Singleton
class SyncRepositoryImpl @Inject constructor(
    private val paymentDao: PaymentDao,
    private val clientDao: ClientDao
) : SyncRepository {

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override suspend fun syncAll(userId: String): Result<Unit> {
        return try {
            // 1. Sync Clients
            val localClients = clientDao.getAllActiveClients(userId).first()
            for (client in localClients) {
                uploadClient(client)
            }

            // 2. Fetch Remote Clients and store in Room
            val remoteClientsSnapshot = firestore.collection("users")
                .document(userId)
                .collection("clients")
                .get()
                .await()

            for (doc in remoteClientsSnapshot.documents) {
                val remoteId = doc.getString("id") ?: doc.id
                val name = doc.getString("name") ?: continue
                val company = doc.getString("company")
                val email = doc.getString("email") ?: ""
                val phone = doc.getString("phone") ?: ""
                val initials = doc.getString("initials") ?: name.take(2).uppercase()
                val isArchived = doc.getBoolean("isArchived") ?: false

                val client = ClientEntity(
                    id = remoteId,
                    userId = userId,
                    name = name,
                    company = company,
                    email = email,
                    phone = phone,
                    initials = initials,
                    isArchived = isArchived
                )
                clientDao.insertClient(client)
            }

            // 3. Sync Payments
            val localPayments = paymentDao.getAllPayments(userId).first()
            for (payment in localPayments) {
                uploadPayment(payment)
            }

            // 4. Fetch Remote Payments and store in Room
            val remotePaymentsSnapshot = firestore.collection("users")
                .document(userId)
                .collection("payments")
                .get()
                .await()

            for (doc in remotePaymentsSnapshot.documents) {
                val remoteId = doc.getString("id") ?: doc.id
                val clientId = doc.getString("clientId") ?: continue
                val clientName = doc.getString("clientName") ?: ""
                val clientEmail = doc.getString("clientEmail") ?: ""
                val clientPhone = doc.getString("clientPhone") ?: ""
                val clientInitials = doc.getString("clientInitials") ?: "CL"
                val invoiceNumber = doc.getString("invoiceNumber") ?: ""
                val scopeDescription = doc.getString("scopeDescription") ?: ""
                val amountCents = doc.getLong("amountCents") ?: 0L
                val amountPaidCents = doc.getLong("amountPaidCents") ?: 0L
                val currency = doc.getString("currency") ?: "USD"
                val issueDateMillis = doc.getLong("issueDateMillis") ?: System.currentTimeMillis()
                val dueDateMillis = doc.getLong("dueDateMillis") ?: System.currentTimeMillis()
                val statusString = doc.getString("status") ?: "PENDING"
                val status = try { PaymentStatus.valueOf(statusString) } catch (e: Exception) { PaymentStatus.PENDING }
                val smartSequence = doc.getBoolean("smartSequenceEnabled") ?: true

                val payment = PaymentEntity(
                    id = remoteId,
                    userId = userId,
                    clientId = clientId,
                    clientName = clientName,
                    clientEmail = clientEmail,
                    clientPhone = clientPhone,
                    clientInitials = clientInitials,
                    invoiceNumber = invoiceNumber,
                    scopeDescription = scopeDescription,
                    amountCents = amountCents,
                    amountPaidCents = amountPaidCents,
                    currency = currency,
                    issueDateMillis = issueDateMillis,
                    dueDateMillis = dueDateMillis,
                    status = status,
                    smartSequenceEnabled = smartSequence
                )
                paymentDao.insertPayment(payment)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            // Offline or network error: return failure gracefully, Room maintains local cache
            Result.failure(e)
        }
    }

    override suspend fun uploadPayment(payment: PaymentEntity): Result<Unit> {
        return try {
            val paymentMap = hashMapOf(
                "id" to payment.id,
                "userId" to payment.userId,
                "clientId" to payment.clientId,
                "clientName" to payment.clientName,
                "clientEmail" to payment.clientEmail,
                "clientPhone" to payment.clientPhone,
                "clientInitials" to payment.clientInitials,
                "invoiceNumber" to payment.invoiceNumber,
                "scopeDescription" to payment.scopeDescription,
                "amountCents" to payment.amountCents,
                "amountPaidCents" to payment.amountPaidCents,
                "currency" to payment.currency,
                "issueDateMillis" to payment.issueDateMillis,
                "dueDateMillis" to payment.dueDateMillis,
                "status" to payment.status.name,
                "smartSequenceEnabled" to payment.smartSequenceEnabled,
                "channelEmail" to payment.channelEmail,
                "channelWhatsApp" to payment.channelWhatsApp,
                "channelSms" to payment.channelSms,
                "updatedAt" to System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(payment.userId)
                .collection("payments")
                .document(payment.id)
                .set(paymentMap, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadClient(client: ClientEntity): Result<Unit> {
        return try {
            val clientMap = hashMapOf(
                "id" to client.id,
                "userId" to client.userId,
                "name" to client.name,
                "company" to (client.company ?: ""),
                "email" to client.email,
                "phone" to client.phone,
                "initials" to client.initials,
                "notes" to (client.notes ?: ""),
                "isArchived" to client.isArchived,
                "updatedAt" to System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(client.userId)
                .collection("clients")
                .document(client.id)
                .set(clientMap, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
