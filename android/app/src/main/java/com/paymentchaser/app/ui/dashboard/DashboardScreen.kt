package com.paymentchaser.app.ui.dashboard

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paymentchaser.app.data.model.PaymentEntity
import com.paymentchaser.app.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToAddPayment: () -> Unit,
    onNavigateToPaymentDetails: (String) -> Unit,
    onNavigateToPaymentsList: () -> Unit,
    onNavigateToPremium: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToAddPayment,
                containerColor = Secondary,
                contentColor = OnSecondary,
                shape = CircleShape,
                icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Add Payment") },
                text = { Text("+ Track Payment", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Surface)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Greeting & Daily Status Strip
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Good morning, Sarah",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            text = "Friday, Oct 24 • You have ${state.urgentPayments.size} actions today",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onNavigateToPremium,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SecondaryFixed)
                    ) {
                        Icon(
                            Icons.Default.Bolt,
                            contentDescription = "Pro",
                            tint = OnSecondaryFixed
                        )
                    }
                }

                // Micro Pulse Pill
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHigh)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Secondary)
                    )
                    Text(
                        text = "2 auto-reminders dispatched today",
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurface
                    )
                }
            }

            // 2. Hero Balance Card (Deep Navy)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TOTAL OUTSTANDING BALANCE",
                                style = MaterialTheme.typography.labelSmall,
                                color = OnPrimaryContainer,
                                fontWeight = FontWeight.SemiBold
                            )
                            Surface(
                                shape = CircleShape,
                                color = TertiaryContainer
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.ArrowUpward,
                                        contentDescription = null,
                                        tint = TertiaryFixed,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "12% vs last mo",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TertiaryFixed,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Big Amount
                        Row(verticalAlignment = Alignment.Bottom) {
                            val dollars = state.totalOutstandingCents / 100
                            val cents = state.totalOutstandingCents % 100
                            val format = NumberFormat.getCurrencyInstance(Locale.US)
                            Text(
                                text = "$${format.format(dollars).replace("$", "")}",
                                style = MaterialTheme.typography.displayLarge,
                                color = OnPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = ".%02d USD".format(cents),
                                style = MaterialTheme.typography.titleMedium,
                                color = OnPrimaryContainer,
                                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                            )
                        }

                        // Sub stats
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Domain,
                                    contentDescription = null,
                                    tint = TertiaryFixed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Across ${state.activeClientsCount} active clients",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnPrimaryContainer
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Event,
                                    contentDescription = null,
                                    tint = SecondaryFixed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Next incoming: $3,200 tomorrow",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            // 3. 3-Column Metrics Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Overdue
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Overdue",
                        amount = "$${state.totalOverdueCents / 100}",
                        subtext = "2 invoices",
                        badgeText = "Needs Action",
                        badgeBg = ErrorContainer,
                        badgeColor = OnErrorContainer,
                        accentColor = ErrorColor,
                        icon = Icons.Default.Warning,
                        onClick = onNavigateToPaymentsList
                    )

                    // Due Soon
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Due Soon",
                        amount = "$6,100",
                        subtext = "3 in next 5d",
                        badgeText = "Queued",
                        badgeBg = SecondaryFixed,
                        badgeColor = OnSecondaryFixed,
                        accentColor = Secondary,
                        icon = Icons.Default.Schedule,
                        onClick = onNavigateToPaymentsList
                    )

                    // Collected
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Collected",
                        amount = "$${state.totalCollectedCents / 100}",
                        subtext = "This month",
                        badgeText = "100% on-time",
                        badgeBg = TertiaryFixed.copy(alpha = 0.4f),
                        badgeColor = OnTertiaryContainer,
                        accentColor = OnTertiaryContainer,
                        icon = Icons.Default.Verified,
                        onClick = onNavigateToPaymentsList
                    )
                }
            }

            // 4. Requires Attention Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Requires Attention",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Surface(
                            shape = CircleShape,
                            color = ErrorContainer
                        ) {
                            Text(
                                text = "${state.urgentPayments.size}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = OnErrorContainer
                            )
                        }
                    }

                    TextButton(onClick = onNavigateToPaymentsList) {
                        Text(
                            text = "View All",
                            color = Secondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // 5. Urgent Invoices List
            items(state.urgentPayments) { payment ->
                UrgentInvoiceCard(
                    payment = payment,
                    onClick = { onNavigateToPaymentDetails(payment.id) },
                    onSendWhatsApp = { /* Triggers WhatsApp Intent */ },
                    onSendEmail = { /* Triggers Mailto Intent */ }
                )
            }

            // 6. Smart Chaser Engine Automation Queue
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Secondary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Autorenew,
                                        contentDescription = null,
                                        tint = OnSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Smart Chaser Engine",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurface
                                    )
                                    Text(
                                        text = "Active • 3 escalation tiers",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = OnSurfaceVariant
                                    )
                                }
                            }

                            Switch(
                                checked = state.isAutopilotActive,
                                onCheckedChange = { viewModel.toggleAutopilot() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = OnPrimary,
                                    checkedTrackColor = Secondary
                                )
                            )
                        }

                        // Next Pulse
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = SurfaceContainerLowest
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    Icons.Default.MailOutline,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Column {
                                    Text(
                                        text = "Next Action: Friendly nudge to Nexus Digital",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = OnSurface
                                    )
                                    Text(
                                        text = "Dispatches automatically in 18 hrs",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = OnSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: String,
    subtext: String,
    badgeText: String,
    badgeBg: Color,
    badgeColor: Color,
    accentColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
            }

            Text(
                text = amount,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )

            Text(
                text = subtext,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariant,
                fontSize = 11.sp
            )

            Surface(
                shape = CircleShape,
                color = badgeBg,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(
                    text = badgeText,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = badgeColor
                )
            }
        }
    }
}

@Composable
fun UrgentInvoiceCard(
    payment: PaymentEntity,
    onClick: () -> Unit,
    onSendWhatsApp: () -> Unit,
    onSendEmail: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = payment.clientInitials,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceVariant
                        )
                    }

                    Column {
                        Text(
                            text = payment.clientName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            text = payment.scopeDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${payment.amountDecimal}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Surface(
                        shape = CircleShape,
                        color = ErrorContainer,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "7d overdue",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = OnErrorContainer
                        )
                    }
                }
            }

            // Quick actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onSendWhatsApp,
                    modifier = Modifier.weight(1f),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                ) {
                    Icon(
                        Icons.Default.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Send Firm WhatsApp", fontWeight = FontWeight.SemiBold)
                }

                IconButton(
                    onClick = onSendEmail,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHigh)
                ) {
                    Icon(Icons.Default.Email, contentDescription = "Email", tint = OnSurface)
                }

                IconButton(
                    onClick = onClick,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHigh)
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Details", tint = OnSurface)
                }
            }
        }
    }
}
