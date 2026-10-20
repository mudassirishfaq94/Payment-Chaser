package com.paymentchaser.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paymentchaser.app.data.repository.AuthRepository
import com.paymentchaser.app.data.repository.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val businessName: String = "Sarah Studio Design",
    val defaultCurrency: String = "USD",
    val isSyncing: Boolean = false,
    val syncStatusMessage: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {

    private val currentUserId = "user_default"
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun updateBusinessDetails(name: String, currency: String) {
        _uiState.value = _uiState.value.copy(
            businessName = name,
            defaultCurrency = currency
        )
    }

    fun syncNow() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true, syncStatusMessage = null)
            val result = syncRepository.syncAll(currentUserId)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isSyncing = false, syncStatusMessage = "Sync completed successfully")
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(isSyncing = false, syncStatusMessage = "Sync failed: ${it.localizedMessage ?: "Offline"}")
                }
            )
        }
    }

    fun signOut(onSignedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.signOut()
            onSignedOut()
        }
    }

    fun deleteAccount(onDeleted: () -> Unit) {
        viewModelScope.launch {
            authRepository.deleteAccount().onSuccess {
                onDeleted()
            }
        }
    }
}
