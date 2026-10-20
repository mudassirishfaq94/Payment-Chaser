package com.paymentchaser.app.ui.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paymentchaser.app.data.model.ClientEntity
import com.paymentchaser.app.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClientsUiState(
    val clients: List<ClientEntity> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {

    private val currentUserId = "user_default"
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<ClientsUiState> = combine(
        repository.getClients(currentUserId),
        _searchQuery
    ) { allClients, query ->
        val filtered = if (query.isBlank()) {
            allClients
        } else {
            allClients.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.email.contains(query, ignoreCase = true) ||
                (it.company?.contains(query, ignoreCase = true) ?: false)
            }
        }
        ClientsUiState(clients = filtered, searchQuery = query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ClientsUiState())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addClient(name: String, company: String?, email: String, phone: String, notes: String?) {
        viewModelScope.launch {
            val client = ClientEntity(
                id = "client_${System.currentTimeMillis()}",
                userId = currentUserId,
                name = name,
                company = company,
                email = email,
                phone = phone,
                initials = name.take(2).uppercase(),
                notes = notes
            )
            repository.insertClient(client)
        }
    }

    fun updateClient(client: ClientEntity) {
        viewModelScope.launch {
            repository.updateClient(client)
        }
    }

    fun archiveClient(client: ClientEntity, onError: (String) -> Unit) {
        viewModelScope.launch {
            repository.archiveClient(client).onFailure {
                onError(it.localizedMessage ?: "Cannot archive client with active invoices")
            }
        }
    }
}
