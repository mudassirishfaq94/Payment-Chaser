package com.paymentchaser.app.ui.analytics

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paymentchaser.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Performance & Analytics", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
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
            // Collection Efficiency Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryContainer)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("COLLECTION EFFICIENCY", style = MaterialTheme.typography.labelSmall, color = OnPrimaryContainer, fontWeight = FontWeight.SemiBold)
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text("${state.collectionRatePercent}%", style = MaterialTheme.typography.displayLarge, color = TertiaryFixed, fontWeight = FontWeight.Bold)
                            Text(" on-time recovery", style = MaterialTheme.typography.titleMedium, color = OnPrimaryContainer, modifier = Modifier.padding(bottom = 6.dp, start = 6.dp))
                        }
                        LinearProgressIndicator(
                            progress = { state.collectionRatePercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = TertiaryFixed,
                            trackColor = SurfaceContainerHigh
                        )
                    }
                }
            }

            // High Level Metrics Grid
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(shape = RoundedCornerShape(14.dp), color = SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Collected", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$${state.totalCollectedCents / 100}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = OnTertiaryContainer)
                        }
                    }

                    Surface(shape = RoundedCornerShape(14.dp), color = SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Outstanding", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$${state.totalOutstandingCents / 100}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Primary)
                        }
                    }

                    Surface(shape = RoundedCornerShape(14.dp), color = SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Overdue", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$${state.totalOverdueCents / 100}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ErrorColor)
                        }
                    }
                }
            }

            // Largest Overdue Accounts
            item {
                Text("Largest Overdue Accounts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            if (state.topOverduePayments.isEmpty()) {
                item {
                    Text("No overdue accounts currently.", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                }
            } else {
                items(state.topOverduePayments) { payment ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(payment.clientName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                Text("Invoice #${payment.invoiceNumber}", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                            }
                            Text("$${payment.remainingDecimal}", fontWeight = FontWeight.Bold, color = ErrorColor, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}
