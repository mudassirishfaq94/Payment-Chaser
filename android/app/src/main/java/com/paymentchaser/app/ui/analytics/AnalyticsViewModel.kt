package com.paymentchaser.app.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paymentchaser.app.data.model.PaymentEntity
import com.paymentchaser.app.data.model.PaymentStatus
import com.paymentchaser.app.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class AnalyticsUiState(
    val totalCollectedCents: Long = 0L,
    val totalOutstandingCents: Long = 0L,
    val totalOverdueCents: Long = 0L,
    val collectionRatePercent: Int = 0,
    val topOverduePayments: List<PaymentEntity> = emptyList(),
    val totalInvoicesTracked: Int = 0
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {

    private val currentUserId = "user_default"

    val uiState: StateFlow<AnalyticsUiState> = combine(
        repository.getAllPayments(currentUserId),
        repository.observeTotalOutstandingCents(currentUserId),
        repository.observeTotalOverdueCents(currentUserId),
        repository.observeTotalCollectedCents(currentUserId)
    ) { allPayments, outstanding, overdue, collected ->
        val outCents = outstanding ?: 0L
        val collCents = collected ?: 0L
        val totalAll = outCents + collCents
        val rate = if (totalAll > 0) ((collCents.toDouble() / totalAll.toDouble()) * 100).toInt() else 100

        val topOverdue = allPayments
            .filter { it.status == PaymentStatus.OVERDUE || it.isOverdue() }
            .sortedByDescending { it.remainingCents }
            .take(5)

        AnalyticsUiState(
            totalCollectedCents = collCents,
            totalOutstandingCents = outCents,
            totalOverdueCents = overdue ?: 0L,
            collectionRatePercent = rate,
            topOverduePayments = topOverdue,
            totalInvoicesTracked = allPayments.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnalyticsUiState())
}
