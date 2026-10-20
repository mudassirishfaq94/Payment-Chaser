package com.paymentchaser.app.ui.reminders

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SequenceStepItem(
    val id: Int,
    val title: String,
    val timingTag: String,
    val channels: String,
    var templateText: String,
    var isActive: Boolean
)

data class SmartRemindersUiState(
    val isAutopilotEnabled: Boolean = true,
    val selectedTone: String = "FRIENDLY", // "FRIENDLY", "CASUAL", "STRICT"
    val steps: List<SequenceStepItem> = listOf(
        SequenceStepItem(1, "3 Days Before Due Date", "Friendly Heads-up", "Email • SMS", "Hi [Client], just a gentle note that invoice #[Invoice] for [Amount] is due this Friday...", true),
        SequenceStepItem(2, "On Due Date (Day 0)", "Polite Reminder", "Email", "Hi [Client], invoice #[Invoice] is due today. Here is the direct link to settle online...", true),
        SequenceStepItem(3, "3 Days Overdue", "Follow-up Notice", "Email • WhatsApp", "Hi [Client], checking in on invoice #[Invoice] which was due on [DueDate]. Please confirm status...", true),
        SequenceStepItem(4, "7+ Days Overdue", "Urgent Notice", "Escalated Email • WhatsApp", "Attention [Client]: Invoice #[Invoice] is now 7 days overdue. Immediate remittance required...", true)
    )
)

@HiltViewModel
class SmartRemindersViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SmartRemindersUiState())
    val uiState: StateFlow<SmartRemindersUiState> = _uiState.asStateFlow()

    fun toggleAutopilot() {
        _uiState.value = _uiState.value.copy(
            isAutopilotEnabled = !_uiState.value.isAutopilotEnabled
        )
    }

    fun selectTone(tone: String) {
        _uiState.value = _uiState.value.copy(selectedTone = tone)
    }

    fun toggleStep(stepId: Int) {
        val updated = _uiState.value.steps.map {
            if (it.id == stepId) it.copy(isActive = !it.isActive) else it
        }
        _uiState.value = _uiState.value.copy(steps = updated)
    }

    fun updateStepCopy(stepId: Int, newCopy: String) {
        val updated = _uiState.value.steps.map {
            if (it.id == stepId) it.copy(templateText = newCopy) else it
        }
        _uiState.value = _uiState.value.copy(steps = updated)
    }
}
