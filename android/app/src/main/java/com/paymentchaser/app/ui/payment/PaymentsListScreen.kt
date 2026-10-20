package com.paymentchaser.app.ui.payment

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paymentchaser.app.data.model.PaymentEntity
import com.paymentchaser.app.data.model.PaymentStatus
import com.paymentchaser.app.ui.theme.*
import com.paymentchaser.app.util.ShareIntentsHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsListScreen(
    viewModel: PaymentsListViewModel,
    onNavigateToDetails: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var selectedChasePayment by remember { mutableStateOf<PaymentEntity?>(null) }
    var selectedPartialPayment by remember { mutableStateOf<PaymentEntity?>(null) }
    var partialAmountInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Surface)) {
                // Top Search Bar
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search client, invoice #, amount...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Outline) },
                    trailingIcon = {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Cancel, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = CircleShape,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    ),
                    singleLine = true
                )

                // Scrollable Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filterOptions = listOf("ALL" to "All", "OVERDUE" to "Overdue", "PENDING" to "Pending", "PAID" to "Paid")
                    filterOptions.forEach { (key, label) ->
                        val isSelected = state.selectedFilter == key
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) Primary else SurfaceContainerLowest,
                            shadowElevation = 1.dp,
                            modifier = Modifier.clickable { viewModel.setFilter(key) }
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) OnPrimary else OnSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                // Summary Strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerLow)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ErrorColor))
                        Text(
                            "$${state.totalOutstandingCents / 100}.00 uncollected",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        "${state.payments.size} invoices",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariant
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Surface)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.payments.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = OutlineVariant, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No invoices found", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Add an outstanding payment to begin tracking", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                    }
                }
            } else {
                items(state.payments) { payment ->
                    PaymentRowCard(
                        payment = payment,
                        onClick = { onNavigateToDetails(payment.id) },
                        onChaseNow = { selectedChasePayment = payment },
                        onRecordPartial = { selectedPartialPayment = payment },
                        onMarkPaid = { viewModel.markPaymentPaid(payment.id) }
                    )
                }
            }
        }
    }

    // Quick Chase Bottom Sheet
    if (selectedChasePayment != null) {
        val p = selectedChasePayment!!
        val message = "Hi ${p.clientName}, following up regarding invoice #${p.invoiceNumber} for $${p.remainingDecimal}. Please let us know when payment is initiated. Thank you!"

        ModalBottomSheet(onDismissRequest = { selectedChasePayment = null }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Quick Chaser Dispatch", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Sending reminder for invoice #${p.invoiceNumber} ($${p.remainingDecimal})", style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(message, modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            ShareIntentsHelper.openWhatsAppChat(context, p.clientPhone, message)
                            selectedChasePayment = null
                        },
                        modifier = Modifier.weight(1f),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("WhatsApp")
                    }

                    Button(
                        onClick = {
                            ShareIntentsHelper.openEmailDraft(context, p.clientEmail, "Invoice #${p.invoiceNumber} Reminder", message)
                            selectedChasePayment = null
                        },
                        modifier = Modifier.weight(1f),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Email")
                    }
                }
            }
        }
    }

    // Record Partial Payment Dialog
    if (selectedPartialPayment != null) {
        val p = selectedPartialPayment!!
        AlertDialog(
            onDismissRequest = { selectedPartialPayment = null },
            title = { Text("Record Partial Payment") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Client: ${p.clientName} (Remaining: $${p.remainingDecimal})")
                    OutlinedTextField(
                        value = partialAmountInput,
                        onValueChange = { partialAmountInput = it },
                        label = { Text("Amount Received ($)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val dollars = partialAmountInput.toDoubleOrNull() ?: 0.0
                        val cents = (dollars * 100).toLong()
                        if (cents > 0) {
                            viewModel.recordPartialPayment(p.id, cents)
                            selectedPartialPayment = null
                            partialAmountInput = ""
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedPartialPayment = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PaymentRowCard(
    payment: PaymentEntity,
    onClick: () -> Unit,
    onChaseNow: () -> Unit,
    onRecordPartial: () -> Unit,
    onMarkPaid: () -> Unit
) {
    val isOverdue = payment.status == PaymentStatus.OVERDUE || payment.isOverdue()
    val isPaid = payment.status == PaymentStatus.PAID
    val accentColor = if (isPaid) OnTertiaryContainer else if (isOverdue) ErrorColor else Secondary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left Status Accent Bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(accentColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(payment.clientInitials, fontWeight = FontWeight.Bold, color = accentColor)
                        }

                        Column {
                            Text(payment.clientName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("${payment.scopeDescription} • #${payment.invoiceNumber}", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$${payment.remainingDecimal}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = CircleShape,
                            color = if (isPaid) TertiaryFixed.copy(alpha = 0.4f) else if (isOverdue) ErrorContainer else SecondaryFixed,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = if (isPaid) "Paid in Full" else if (isOverdue) "Overdue" else "Due Soon",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isPaid) OnTertiaryContainer else if (isOverdue) OnErrorContainer else OnSecondaryFixed
                            )
                        }
                    }
                }

                // Action buttons if not settled
                if (!isPaid) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onChaseNow,
                            modifier = Modifier.weight(1f),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                        ) {
                            Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Chase Now", fontSize = 12.sp)
                        }

                        FilledTonalButton(
                            onClick = onRecordPartial,
                            shape = CircleShape
                        ) {
                            Text("Partial", fontSize = 12.sp)
                        }

                        FilledTonalButton(
                            onClick = onMarkPaid,
                            shape = CircleShape
                        ) {
                            Text("Mark Paid", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
