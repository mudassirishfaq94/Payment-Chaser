package com.paymentchaser.app.ui.reminders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paymentchaser.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartRemindersScreen(
    viewModel: SmartRemindersViewModel
) {
    val state by viewModel.uiState.collectAsState()
    var editingStepId by remember { mutableStateOf<Int?>(null) }
    var editCopyText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Reminders", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Surface)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Master Autopilot Bar
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(SecondaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = OnSecondaryFixed)
                            }
                            Column {
                                Text("Master Autopilot", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Automated multi-channel cadence", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                            }
                        }

                        Switch(
                            checked = state.isAutopilotEnabled,
                            onCheckedChange = { viewModel.toggleAutopilot() },
                            colors = SwitchDefaults.colors(checkedTrackColor = Secondary)
                        )
                    }
                }
            }

            // Warm Philosophy Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryContainer)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(shape = CircleShape, color = SecondaryContainer.copy(alpha = 0.4f)) {
                            Text("Zero Friction Cadence", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), color = SecondaryFixed, style = MaterialTheme.typography.labelSmall)
                        }
                        Text("Effortless, Polite & Effective", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = OnPrimary)
                        Text(
                            "Payment Chaser automatically calculates follow-ups so you never have to endure the awkwardness of manual chasing.",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnPrimaryContainer
                        )
                    }
                }
            }

            // Communication Tone Selector
            item {
                Text("Communication Tone", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val tones = listOf(
                        Triple("FRIENDLY", "Friendly & Professional (Recommended)", "Firm yet polite. Keeps client relationships warm while driving prompt settlements."),
                        Triple("CASUAL", "Casual Freelancer", "Warm, peer-to-peer phrasing tailored for solo creators and studios."),
                        Triple("STRICT", "Strict Corporate", "Formal and structured. Emphasizes contractual milestones and late fee terms.")
                    )

                    tones.forEach { (key, title, desc) ->
                        val isSelected = state.selectedTone == key
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectTone(key) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = if (isSelected) SurfaceContainerLowest else SurfaceContainerLow),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Secondary) else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(desc, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Sequence Timeline
            item {
                Text("Sequence Timeline", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            items(state.steps) { step ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (step.isActive) Secondary else OutlineVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${step.id}", color = OnPrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                                }
                                Text(step.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                            }
                            Switch(checked = step.isActive, onCheckedChange = { viewModel.toggleStep(step.id) })
                        }

                        Text("Channels: ${step.channels}", style = MaterialTheme.typography.labelSmall, color = Secondary)

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SurfaceContainerLow,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "“${step.templateText}”",
                                modifier = Modifier.padding(10.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurfaceVariant
                            )
                        }

                        TextButton(
                            onClick = {
                                editingStepId = step.id
                                editCopyText = step.templateText
                            }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Customize Copy")
                        }
                    }
                }
            }
        }
    }

    // Edit Template Dialog
    if (editingStepId != null) {
        AlertDialog(
            onDismissRequest = { editingStepId = null },
            title = { Text("Customize Template Message") },
            text = {
                OutlinedTextField(
                    value = editCopyText,
                    onValueChange = { editCopyText = it },
                    label = { Text("Template text with [Client], [Amount], [Invoice]") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.updateStepCopy(editingStepId!!, editCopyText)
                    editingStepId = null
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingStepId = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
