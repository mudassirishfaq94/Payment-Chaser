package com.paymentchaser.app.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paymentchaser.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentScreen(
    onNavigateBack: () -> Unit,
    onSavePayment: (amountCents: Long, clientName: String, dueDateMillis: Long, invoiceNo: String) -> Unit
) {
    var amountInput by remember { mutableStateOf("") }
    var selectedClient by remember { mutableStateOf("Acme Studio") }
    var clientContact by remember { mutableStateOf("contact@acmestudio.com • +1 (555) 019-2834") }
    var dueTag by remember { mutableStateOf("Net 14 Days") }
    var invoiceRef by remember { mutableStateOf("INV-2025-089") }
    var scopeDescription by remember { mutableStateOf("Q4 Design System Sprint & Brand System") }
    var isSmartSequenceEnabled by remember { mutableStateOf(true) }

    var emailEnabled by remember { mutableStateOf(true) }
    var whatsappEnabled by remember { mutableStateOf(true) }
    var smsEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Track New Payment", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SecondaryContainer))
                            Text("Lightning Log < 30s", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = onNavigateBack,
                        shape = CircleShape,
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = SecondaryFixed, contentColor = OnSecondaryFixed)
                    ) {
                        Icon(Icons.Default.Drafts, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Drafts (1)", fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Surface.copy(alpha = 0.95f),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val dollars = amountInput.toDoubleOrNull() ?: 0.0
                            val cents = (dollars * 100).toLong()
                            onSavePayment(cents, selectedClient, System.currentTimeMillis() + 14 * 86400000L, invoiceRef)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save & Start Tracking", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }

                    TextButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save as Unsent Draft", color = OnSurfaceVariant)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Surface)
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Amount Hero Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Amount to Collect", style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)
                            Surface(shape = CircleShape, color = SurfaceContainer) {
                                Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(OnTertiaryContainer))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("USD ($)", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("$", style = MaterialTheme.typography.displayLarge, color = Primary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            TextField(
                                value = amountInput,
                                onValueChange = { amountInput = it },
                                placeholder = { Text("0.00", style = MaterialTheme.typography.displayLarge, color = OutlineVariant) },
                                textStyle = MaterialTheme.typography.displayLarge.copy(color = Primary, fontWeight = FontWeight.Bold),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        }

                        // Presets
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(500, 1000, 2500, 5000).forEach { preset ->
                                Surface(
                                    shape = CircleShape,
                                    color = SurfaceContainer,
                                    modifier = Modifier.clickable {
                                        val current = amountInput.toDoubleOrNull() ?: 0.0
                                        amountInput = (current + preset).toString()
                                    }
                                ) {
                                    Text("+$${preset}", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }
            }

            // Client Counterparty Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Client Counterparty", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text("Tap to autofill", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                        }

                        // Recent chips
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Acme Studio", "Horizon Labs", "Nexus Digital").forEach { name ->
                                val isSelected = selectedClient == name
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) Primary else SurfaceContainer,
                                    modifier = Modifier.clickable { selectedClient = name }
                                ) {
                                    Text(
                                        name,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        color = if (isSelected) OnPrimary else OnSurface,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }

                        // Autofill card
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SurfaceContainerLow,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(SecondaryContainer), contentAlignment = Alignment.Center) {
                                    Text(selectedClient.take(2).uppercase(), color = OnSecondary, fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(selectedClient, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                    Text(clientContact, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                                }
                                Icon(Icons.Default.Verified, contentDescription = null, tint = OnTertiaryContainer, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            // Due Date Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Due Date", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Surface(shape = CircleShape, color = ErrorContainer) {
                                Text(dueTag, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = OnErrorContainer, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Today", "Net 7", "Net 14", "Net 30", "Custom").forEach { tag ->
                                val isSelected = dueTag.startsWith(tag)
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) Primary else SurfaceContainer,
                                    modifier = Modifier.weight(1f).clickable { dueTag = tag }
                                ) {
                                    Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                        Text(tag, color = if (isSelected) OnPrimary else OnSurface, style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Smart Sequence Switch Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(SecondaryContainer), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = OnSecondary, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Activate Smart Sequence", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                    Text("94% on-time recovery rate", style = MaterialTheme.typography.labelSmall, color = OnTertiaryContainer, fontWeight = FontWeight.Bold)
                                }
                            }
                            Switch(checked = isSmartSequenceEnabled, onCheckedChange = { isSmartSequenceEnabled = it })
                        }

                        Text(
                            "Polite multi-stage nudges: 3 days before, on the due date, and gentle escalations if delayed. Zero awkward conversations.",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = CircleShape,
                                color = if (emailEnabled) Primary else SurfaceContainer,
                                modifier = Modifier.clickable { emailEnabled = !emailEnabled }
                            ) {
                                Text("✓ Email Nudge", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = if (emailEnabled) OnPrimary else OnSurface, style = MaterialTheme.typography.labelSmall)
                            }
                            Surface(
                                shape = CircleShape,
                                color = if (whatsappEnabled) Primary else SurfaceContainer,
                                modifier = Modifier.clickable { whatsappEnabled = !whatsappEnabled }
                            ) {
                                Text("✓ WhatsApp Link", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = if (whatsappEnabled) OnPrimary else OnSurface, style = MaterialTheme.typography.labelSmall)
                            }
                            Surface(
                                shape = CircleShape,
                                color = if (smsEnabled) Primary else SurfaceContainer,
                                modifier = Modifier.clickable { smsEnabled = !smsEnabled }
                            ) {
                                Text("+ SMS Fallback", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = if (smsEnabled) OnPrimary else OnSurface, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}
