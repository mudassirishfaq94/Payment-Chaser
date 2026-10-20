package com.paymentchaser.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paymentchaser.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateToReminders: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToPremium: () -> Unit,
    onSignedOut: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showSignOutConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings & Profile", fontWeight = FontWeight.Bold) },
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
            // Profile Card
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
                            Column {
                                Text(state.businessName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Currency: ${state.defaultCurrency}", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                            }
                            Surface(shape = CircleShape, color = SecondaryFixed) {
                                Text("PRO", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = OnSecondaryFixed, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            // Cloud Synchronization Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Cloud Synchronization", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(
                            "Sync local Room records with your private Firestore database for multi-device availability.",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )

                        if (state.syncStatusMessage != null) {
                            Text(state.syncStatusMessage!!, style = MaterialTheme.typography.bodySmall, color = Secondary, fontWeight = FontWeight.Medium)
                        }

                        Button(
                            onClick = { viewModel.syncNow() },
                            enabled = !state.isSyncing,
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            if (state.isSyncing) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = OnPrimary)
                            } else {
                                Icon(Icons.Default.CloudSync, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Sync Now")
                            }
                        }
                    }
                }
            }

            // Feature Shortcuts
            item {
                Text("Preferences & Add-ons", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
                ) {
                    Column {
                        ListItem(
                            headlineContent = { Text("Smart Reminders Cadence", fontWeight = FontWeight.SemiBold) },
                            supportingContent = { Text("Multi-stage follow-up schedule and tones") },
                            leadingContent = { Icon(Icons.Default.Schedule, contentDescription = null, tint = Secondary) },
                            trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                            modifier = Modifier.clickable { onNavigateToReminders() }
                        )
                        HorizontalDivider(color = SurfaceContainerLow)
                        ListItem(
                            headlineContent = { Text("Performance & Analytics", fontWeight = FontWeight.SemiBold) },
                            supportingContent = { Text("Collection rates and debtor insights") },
                            leadingContent = { Icon(Icons.Default.Analytics, contentDescription = null, tint = Secondary) },
                            trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                            modifier = Modifier.clickable { onNavigateToAnalytics() }
                        )
                        HorizontalDivider(color = SurfaceContainerLow)
                        ListItem(
                            headlineContent = { Text("Payment Chaser Pro", fontWeight = FontWeight.SemiBold) },
                            supportingContent = { Text("Manage Google Play subscription") },
                            leadingContent = { Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = OnTertiaryContainer) },
                            trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                            modifier = Modifier.clickable { onNavigateToPremium() }
                        )
                    }
                }
            }

            // Account Actions
            item {
                Button(
                    onClick = { showSignOutConfirm = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorContainer, contentColor = OnErrorContainer)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Sign Out", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showSignOutConfirm) {
        AlertDialog(
            onDismissRequest = { showSignOutConfirm = false },
            title = { Text("Sign Out?") },
            text = { Text("Your local data is safely stored in Room and synced with Firestore.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.signOut(onSignedOut)
                        showSignOutConfirm = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
