package com.paymentchaser.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paymentchaser.app.data.model.PaymentEntity
import com.paymentchaser.app.data.model.PaymentStatus
import com.paymentchaser.app.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val totalOutstandingCents: Long = 0L,
    val totalOverdueCents: Long = 0L,
    val totalCollectedCents: Long = 0L,
    val activeClientsCount: Int = 0,
    val urgentPayments: List<PaymentEntity> = emptyList(),
    val isAutopilotActive: Boolean = true,
    val isLoading: Boolean = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val currentUserId = "user_default" // Tied to FirebaseAuth in production

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            combine(
                repository.getAllPayments(currentUserId),
                repository.getClients(currentUserId),
                repository.observeTotalOutstandingCents(currentUserId),
                repository.observeTotalOverdueCents(currentUserId),
                repository.observeTotalCollectedCents(currentUserId)
            ) { payments, clients, outstanding, overdue, collected ->
                val urgent = payments.filter {
                    it.status == PaymentStatus.OVERDUE || it.status == PaymentStatus.DUE_SOON
                }.take(3)

                DashboardUiState(
                    totalOutstandingCents = outstanding ?: 0L,
                    totalOverdueCents = overdue ?: 0L,
                    totalCollectedCents = collected ?: 0L,
                    activeClientsCount = clients.size,
                    urgentPayments = urgent,
                    isAutopilotActive = true,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun toggleAutopilot() {
        _uiState.value = _uiState.value.copy(
            isAutopilotActive = !_uiState.value.isAutopilotActive
        )
    }

    fun markPaymentPaid(paymentId: String) {
        viewModelScope.launch {
            repository.markPaymentAsPaid(paymentId)
        }
    }
}
