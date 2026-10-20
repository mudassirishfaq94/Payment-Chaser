package com.paymentchaser.app.ui.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paymentchaser.app.data.model.PaymentEntity
import com.paymentchaser.app.data.model.ReminderLogEntity
import com.paymentchaser.app.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentDetailsUiState(
    val payment: PaymentEntity? = null,
    val reminderLogs: List<ReminderLogEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class PaymentDetailsViewModel @Inject constructor(
    private val repository: PaymentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val paymentId: String = savedStateHandle["paymentId"] ?: ""

    val uiState: StateFlow<PaymentDetailsUiState> = combine(
        repository.observePayment(paymentId),
        repository.getReminderLogs(paymentId)
    ) { payment, logs ->
        PaymentDetailsUiState(
            payment = payment,
            reminderLogs = logs,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PaymentDetailsUiState())

    fun recordPayment(centsToAdd: Long) {
        viewModelScope.launch {
            repository.recordPartialPayment(paymentId, centsToAdd)
        }
    }

    fun markPaid() {
        viewModelScope.launch {
            repository.markPaymentAsPaid(paymentId)
        }
    }

    fun deletePayment(onDeleted: () -> Unit) {
        val p = uiState.value.payment ?: return
        viewModelScope.launch {
            repository.deletePayment(p)
            onDeleted()
        }
    }

    fun logReminderAction(channel: String, actionType: String, messageSnippet: String) {
        val p = uiState.value.payment ?: return
        viewModelScope.launch {
            repository.logReminderAction(
                ReminderLogEntity(
                    id = "log_${System.currentTimeMillis()}",
                    paymentId = p.id,
                    clientName = p.clientName,
                    channel = channel,
                    actionType = actionType,
                    messageSnippet = messageSnippet
                )
            )
        }
    }
}
