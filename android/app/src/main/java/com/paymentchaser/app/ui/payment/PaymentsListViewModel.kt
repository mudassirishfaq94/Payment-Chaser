package com.paymentchaser.app.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paymentchaser.app.data.model.PaymentEntity
import com.paymentchaser.app.data.model.PaymentStatus
import com.paymentchaser.app.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentsListUiState(
    val payments: List<PaymentEntity> = emptyList(),
    val totalOutstandingCents: Long = 0L,
    val selectedFilter: String = "ALL", // "ALL", "OVERDUE", "PENDING", "PAID"
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class PaymentsListViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {

    private val currentUserId = "user_default"
    private val _selectedFilter = MutableStateFlow("ALL")
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<PaymentsListUiState> = combine(
        repository.getAllPayments(currentUserId),
        repository.observeTotalOutstandingCents(currentUserId),
        _selectedFilter,
        _searchQuery
    ) { allPayments, outstanding, filter, query ->
        val filtered = allPayments.filter { payment ->
            val matchesFilter = when (filter) {
                "OVERDUE" -> payment.status == PaymentStatus.OVERDUE || payment.isOverdue()
                "PENDING" -> payment.status == PaymentStatus.PENDING || payment.status == PaymentStatus.DUE_SOON
                "PAID" -> payment.status == PaymentStatus.PAID
                else -> true
            }

            val matchesQuery = query.isBlank() ||
                payment.clientName.contains(query, ignoreCase = true) ||
                payment.invoiceNumber.contains(query, ignoreCase = true) ||
                payment.scopeDescription.contains(query, ignoreCase = true)

            matchesFilter && matchesQuery
        }

        PaymentsListUiState(
            payments = filtered,
            totalOutstandingCents = outstanding ?: 0L,
            selectedFilter = filter,
            searchQuery = query,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PaymentsListUiState())

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun recordPartialPayment(paymentId: String, centsToAdd: Long) {
        viewModelScope.launch {
            repository.recordPartialPayment(paymentId, centsToAdd)
        }
    }

    fun markPaymentPaid(paymentId: String) {
        viewModelScope.launch {
            repository.markPaymentAsPaid(paymentId)
        }
    }
}
