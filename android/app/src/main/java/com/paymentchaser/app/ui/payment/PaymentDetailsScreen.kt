package com.paymentchaser.app.ui.payment

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paymentchaser.app.ui.theme.*
import com.paymentchaser.app.util.ShareIntentsHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentDetailsScreen(
    viewModel: PaymentDetailsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val payment = state.payment

    var showRecordDialog by remember { mutableStateOf(false) }
    var recordAmountInput by remember { mutableStateOf("") }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(payment?.let { "#${it.invoiceNumber}" } ?: "Invoice Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = ErrorColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface)
            )
        }
    ) { padding ->
        if (payment == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val isOverdue = payment.isOverdue()
            val isPaid = payment.status.name == "PAID"

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Surface)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Client Info Card
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
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(payment.clientInitials, color = OnPrimary, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(payment.clientName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(payment.scopeDescription, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${payment.clientEmail} • ${payment.clientPhone}", style = MaterialTheme.typography.bodySmall, color = Outline)
                            }
                        }
                    }
                }

                // Balance Hero Card (Deep Navy)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = PrimaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("TOTAL OUTSTANDING", style = MaterialTheme.typography.labelSmall, color = OnPrimaryContainer, fontWeight = FontWeight.SemiBold)
                                Surface(
                                    shape = CircleShape,
                                    color = if (isPaid) TertiaryContainer else ErrorContainer
                                ) {
                                    Text(
                                        text = if (isPaid) "Paid in Full" else if (isOverdue) "Overdue" else "Due Soon",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPaid) TertiaryFixed else OnErrorContainer
                                    )
                                }
                            }

                            Text(
                                text = "$${payment.remainingDecimal} ${payment.currency}",
                                style = MaterialTheme.typography.displayMedium,
                                color = OnPrimary,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(shape = RoundedCornerShape(8.dp), color = SurfaceContainerLowest.copy(alpha = 0.1f), modifier = Modifier.weight(1f)) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text("Original", style = MaterialTheme.typography.labelSmall, color = OnPrimaryContainer)
                                        Text("$${payment.amountDecimal}", color = OnPrimary, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Surface(shape = RoundedCornerShape(8.dp), color = SurfaceContainerLowest.copy(alpha = 0.1f), modifier = Modifier.weight(1f)) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text("Received", style = MaterialTheme.typography.labelSmall, color = OnPrimaryContainer)
                                        Text("$${payment.amountPaidDecimal}", color = TertiaryFixed, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // Primary Action Bar
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (!isPaid) {
                            Button(
                                onClick = {
                                    val msg = "Hi ${payment.clientName}, reminder regarding invoice #${payment.invoiceNumber} for $${payment.remainingDecimal}."
                                    ShareIntentsHelper.openWhatsAppChat(context, payment.clientPhone, msg)
                                    viewModel.logReminderAction("WhatsApp", "OPENED_FOR_SHARING", msg)
                                },
                                modifier = Modifier.weight(1f),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                            ) {
                                Icon(Icons.Default.Bolt, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Send Reminder", fontWeight = FontWeight.Bold)
                            }

                            FilledTonalButton(
                                onClick = { showRecordDialog = true },
                                shape = CircleShape
                            ) {
                                Text("Record Payment")
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = TertiaryFixed.copy(alpha = 0.3f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = OnTertiaryContainer)
                                    Text("This invoice is settled in full.", fontWeight = FontWeight.Bold, color = OnTertiaryContainer)
                                }
                            }
                        }
                    }
                }

                // Timeline & Reminder Logs Header
                item {
                    Text("Timeline & Audit Trail", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                if (state.reminderLogs.isEmpty()) {
                    item {
                        Text("No external follow-ups logged yet.", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                    }
                } else {
                    items(state.reminderLogs) { log ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SurfaceContainerLowest,
                            modifier = Modifier.fillMaxWidth(),
                            shadowElevation = 1.dp
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("${log.channel} Follow-Up (${log.actionType})", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(log.messageSnippet, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }

    // Record Payment Dialog
    if (showRecordDialog && payment != null) {
        AlertDialog(
            onDismissRequest = { showRecordDialog = false },
            title = { Text("Record Received Payment") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Remaining balance: $${payment.remainingDecimal}")
                    OutlinedTextField(
                        value = recordAmountInput,
                        onValueChange = { recordAmountInput = it },
                        label = { Text("Amount ($)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val dollars = recordAmountInput.toDoubleOrNull() ?: 0.0
                    val cents = (dollars * 100).toLong()
                    if (cents > 0) {
                        viewModel.recordPayment(cents)
                        showRecordDialog = false
                    }
                }) {
                    Text("Record")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRecordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Payment?") },
            text = { Text("This will permanently remove this invoice and cancel all pending reminders.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deletePayment {
                            showDeleteConfirm = false
                            onNavigateBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorColor)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
