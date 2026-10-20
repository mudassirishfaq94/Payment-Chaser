package com.paymentchaser.app

import com.paymentchaser.app.data.model.PaymentEntity
import com.paymentchaser.app.data.model.PaymentStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class PaymentCalculationTest {

    @Test
    fun decimalSafeMonetaryCalculation_noFloatingPointDrift() {
        val initialAmountCents = 1485000L // $14,850.00
        val payment = PaymentEntity(
            id = "test-1",
            userId = "u1",
            clientId = "c1",
            clientName = "Acme Studio",
            clientEmail = "contact@acme.com",
            clientPhone = "+15550192834",
            clientInitials = "AS",
            invoiceNumber = "INV-1042",
            scopeDescription = "Brand Redesign",
            amountCents = initialAmountCents,
            amountPaidCents = 210000L, // $2,100.00
            issueDateMillis = 1729000000000L,
            dueDateMillis = 1730000000000L,
            status = PaymentStatus.PARTIALLY_PAID
        )

        assertEquals(1275000L, payment.remainingCents)
        assertEquals(BigDecimal("14850.00"), payment.amountDecimal)
        assertEquals(BigDecimal("2100.00"), payment.amountPaidDecimal)
        assertEquals(BigDecimal("12750.00"), payment.remainingDecimal)
    }

    @Test
    fun overdueTransition_calculatesCorrectly() {
        val pastDueDateMillis = System.currentTimeMillis() - (7 * 86400000L) // 7 days in the past
        val payment = PaymentEntity(
            id = "test-2",
            userId = "u1",
            clientId = "c1",
            clientName = "Acme Studio",
            clientEmail = "contact@acme.com",
            clientPhone = "+15550192834",
            clientInitials = "AS",
            invoiceNumber = "INV-1042",
            scopeDescription = "Brand Redesign",
            amountCents = 285000L,
            amountPaidCents = 0L,
            issueDateMillis = pastDueDateMillis - 14 * 86400000L,
            dueDateMillis = pastDueDateMillis,
            status = PaymentStatus.OVERDUE
        )

        assertTrue(payment.isOverdue())
    }

    @Test
    fun paidStatus_isNotOverdue() {
        val pastDueDateMillis = System.currentTimeMillis() - (7 * 86400000L)
        val settledPayment = PaymentEntity(
            id = "test-3",
            userId = "u1",
            clientId = "c1",
            clientName = "Lumina Media",
            clientEmail = "finance@lumina.com",
            clientPhone = "+15559021133",
            clientInitials = "LM",
            invoiceNumber = "INV-1037",
            scopeDescription = "Motion Graphics",
            amountCents = 210000L,
            amountPaidCents = 210000L,
            issueDateMillis = pastDueDateMillis - 14 * 86400000L,
            dueDateMillis = pastDueDateMillis,
            status = PaymentStatus.PAID
        )

        assertEquals(0L, settledPayment.remainingCents)
        assertFalse(settledPayment.isOverdue())
    }

    @Test
    fun multiplePartialPayments_accumulateAccurately() {
        var paidCents = 0L
        val installment1 = 33333L // $333.33
        val installment2 = 33333L // $333.33
        val installment3 = 33334L // $333.34
        paidCents += installment1
        paidCents += installment2
        paidCents += installment3

        assertEquals(100000L, paidCents) // Exactly $1,000.00
    }
}
