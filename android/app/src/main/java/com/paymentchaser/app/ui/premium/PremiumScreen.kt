package com.paymentchaser.app.ui.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paymentchaser.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(
    onNavigateBack: () -> Unit
) {
    var isAnnualSelected by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    Surface(shape = CircleShape, color = SecondaryFixed, modifier = Modifier.padding(end = 12.dp)) {
                        Text("Risk-Free Trial", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), color = OnSecondaryFixed, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface)
            )
        },
        bottomBar = {
            Surface(modifier = Modifier.fillMaxWidth(), color = Surface, shadowElevation = 8.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = {
                            // Initiates Google Play Billing flow in production
                            onNavigateBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start 14-Day Free Trial", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }

                    Text(
                        if (isAnnualSelected) "Then $99/year ($8.25/mo). Cancel easily anytime in Google Play." else "Then $12/month. Billed monthly, cancel anytime in Google Play.",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariant,
                        fontSize = 11.sp
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(shape = CircleShape, color = PrimaryContainer) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = TertiaryFixed, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("PAYMENT CHASER PRO", color = TertiaryFixed, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Get Paid 3x Faster with Zero Awkward Chasing",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Unlock unlimited automated reminders, multi-channel messaging, and advanced cash flow forecasts.",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Plan Selector
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(SurfaceContainerHigh)
                        .padding(4.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (!isAnnualSelected) Primary else SurfaceContainerHigh,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { isAnnualSelected = false }
                    ) {
                        Box(modifier = Modifier.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                            Text("Monthly ($12/mo)", color = if (!isAnnualSelected) OnPrimary else OnSurface, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = if (isAnnualSelected) Primary else SurfaceContainerHigh,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { isAnnualSelected = true }
                    ) {
                        Row(modifier = Modifier.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Text("Annual ($99/yr)", color = if (isAnnualSelected) OnPrimary else OnSurface, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(shape = CircleShape, color = Secondary) {
                                Text("SAVE 31%", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), color = OnSecondary, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Tier Comparison
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Tier Comparison", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                        val features = listOf(
                            Triple("Active Payments", "Max 10", "Unlimited"),
                            Triple("Smart Sequences", "Manual", "Automated"),
                            Triple("WhatsApp Chaser", "Link only", "1-Tap"),
                            Triple("Tone & Escalation", "Standard", "Customizable"),
                            Triple("Analytics & Scoring", "Basic", "Deep Intelligence")
                        )

                        features.forEach { (feat, free, pro) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(feat, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1.5f))
                                Text(free, style = MaterialTheme.typography.labelSmall, color = Outline, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                Text(pro, style = MaterialTheme.typography.labelSmall, color = Secondary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                            }
                            HorizontalDivider(color = SurfaceContainerLow)
                        }
                    }
                }
            }
        }
    }
}
