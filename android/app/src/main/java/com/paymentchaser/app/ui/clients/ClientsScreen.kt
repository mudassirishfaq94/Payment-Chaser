package com.paymentchaser.app.ui.clients

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
import com.paymentchaser.app.data.model.ClientEntity
import com.paymentchaser.app.ui.theme.*
import com.paymentchaser.app.util.ShareIntentsHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsScreen(
    viewModel: ClientsViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showAddDialog by remember { mutableStateOf(false) }
    var editingClient by remember { mutableStateOf<ClientEntity?>(null) }
    var alertErrorMessage by remember { mutableStateOf<String?>(null) }

    var inputName by remember { mutableStateOf("") }
    var inputCompany by remember { mutableStateOf("") }
    var inputEmail by remember { mutableStateOf("") }
    var inputPhone by remember { mutableStateOf("") }
    var inputNotes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Surface)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Client Counterparties", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    IconButton(
                        onClick = {
                            inputName = ""
                            inputCompany = ""
                            inputEmail = ""
                            inputPhone = ""
                            inputNotes = ""
                            editingClient = null
                            showAddDialog = true
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Secondary)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Client", tint = OnSecondary)
                    }
                }

                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search client name, email, company...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Outline) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = CircleShape,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    ),
                    singleLine = true
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Surface)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (state.clients.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Group, contentDescription = null, tint = OutlineVariant, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("No clients found", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Add your first client counterparty using the + button", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                    }
                }
            } else {
                items(state.clients) { client ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
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
                                            .background(SecondaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(client.initials, color = OnSecondary, fontWeight = FontWeight.Bold)
                                    }
                                    Column {
                                        Text(client.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        Text(client.company ?: "Direct Client", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                                    }
                                }

                                Row {
                                    IconButton(onClick = {
                                        editingClient = client
                                        inputName = client.name
                                        inputCompany = client.company ?: ""
                                        inputEmail = client.email
                                        inputPhone = client.phone
                                        inputNotes = client.notes ?: ""
                                        showAddDialog = true
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp), tint = Outline)
                                    }
                                    IconButton(onClick = {
                                        viewModel.archiveClient(client) { error ->
                                            alertErrorMessage = error
                                        }
                                    }) {
                                        Icon(Icons.Default.Archive, contentDescription = "Archive", modifier = Modifier.size(18.dp), tint = Outline)
                                    }
                                }
                            }

                            // Contact links
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                TextButton(
                                    onClick = {
                                        ShareIntentsHelper.openWhatsAppChat(context, client.phone, "Hello ${client.name},")
                                    }
                                ) {
                                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp), tint = OnTertiaryContainer)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("WhatsApp", color = OnTertiaryContainer)
                                }

                                TextButton(
                                    onClick = {
                                        ShareIntentsHelper.openEmailDraft(context, client.email, "Notice from Payment Chaser", "")
                                    }
                                ) {
                                    Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp), tint = Secondary)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Email", color = Secondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Client Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(if (editingClient == null) "Add New Client" else "Edit Client") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = inputName,
                        onValueChange = { inputName = it },
                        label = { Text("Client Name *") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = inputCompany,
                        onValueChange = { inputCompany = it },
                        label = { Text("Company / Brand") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = inputEmail,
                        onValueChange = { inputEmail = it },
                        label = { Text("Billing Email *") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = inputPhone,
                        onValueChange = { inputPhone = it },
                        label = { Text("Phone (for WhatsApp) *") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inputName.isNotBlank() && inputEmail.isNotBlank()) {
                            if (editingClient == null) {
                                viewModel.addClient(inputName, inputCompany, inputEmail, inputPhone, inputNotes)
                            } else {
                                viewModel.updateClient(
                                    editingClient!!.copy(
                                        name = inputName,
                                        company = inputCompany,
                                        email = inputEmail,
                                        phone = inputPhone,
                                        notes = inputNotes
                                    )
                                )
                            }
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Accidental Deletion Protection Alert Dialog
    if (alertErrorMessage != null) {
        AlertDialog(
            onDismissRequest = { alertErrorMessage = null },
            title = { Text("Action Blocked") },
            text = { Text(alertErrorMessage!!) },
            confirmButton = {
                Button(onClick = { alertErrorMessage = null }) {
                    Text("Understood")
                }
            }
        )
    }
}
