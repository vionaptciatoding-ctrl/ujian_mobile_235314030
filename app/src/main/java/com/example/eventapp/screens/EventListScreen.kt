package com.example.eventapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eventapp.data.model.Event
import com.example.eventapp.data.remote.ApiClient

@Composable
fun EventListScreen(
    modifier: Modifier = Modifier,
    onEventClick: (Int) -> Unit,      // ke Detail (CASE 2)
    onFilterClick: (String) -> Unit   // ke EventByDate (CASE 3)
) {
    // list bisa dimodifikasi
    val events = remember { mutableStateListOf<Event>() }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    var filterDate by remember { mutableStateOf("") }

    // state dialog tambah/edit
    var showAddDialog by remember { mutableStateOf(false) }
    var editingEvent by remember { mutableStateOf<Event?>(null) }

    // CASE 1 — GET ALL EVENTS
    LaunchedEffect(Unit) {
        try {
            val response = ApiClient.eventService.getAllEvents()
            if (response.isSuccessful) {
                val data = response.body()?.data ?: emptyList()
                events.clear()
                events.addAll(data)
            } else {
                errorMessage = "Error ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = e.localizedMessage ?: "Unknown error"
        }
        isLoading = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ------------ INPUT FILTER TANGGAL -------------
        OutlinedTextField(
            value = filterDate,
            onValueChange = { filterDate = it },
            label = { Text("Filter berdasarkan tanggal (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { if (filterDate.isNotBlank()) onFilterClick(filterDate) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tampilkan event pada tanggal ini")
        }

        Spacer(Modifier.height(8.dp))

        // ------------ TOMBOL TAMBAH EVENT -------------
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Event")
        }

        Spacer(Modifier.height(16.dp))

        // ------------ LIST EVENT -------------
        when {
            isLoading -> {
                CircularProgressIndicator(Modifier.padding(20.dp))
            }

            errorMessage.isNotEmpty() -> {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {
                LazyColumn {
                    items(events, key = { it.id }) { event ->
                        EventItem(
                            event = event,
                            onClick = { onEventClick(event.id) },
                            onEdit = { editingEvent = event },
                            onDelete = {
                                // Hapus lokal dari list
                                events.removeAll { it.id == event.id }
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }

    // ------------ DIALOG TAMBAH -------------
    if (showAddDialog) {
        EventDialog(
            title = "Tambah Event",
            initialEvent = null,
            onDismiss = { showAddDialog = false },
            onConfirm = { title, date, location, status ->
                // bikin id baru lokal (tidak ke server)
                val newId = (events.maxOfOrNull { it.id } ?: 0) + 1
                events.add(
                    Event(
                        id = newId,
                        title = title,
                        date = date,
                        time = "",          // kosong dulu
                        location = location,
                        description = "",   // kosong dulu
                        capacity = 0,       // default
                        status = status
                    )
                )

                showAddDialog = false
            }
        )
    }

    // ------------ DIALOG EDIT -------------
    editingEvent?.let { event ->
        EventDialog(
            title = "Edit Event",
            initialEvent = event,
            onDismiss = { editingEvent = null },
            onConfirm = { newTitle, newDate, newLocation, newStatus ->
                val index = events.indexOfFirst { it.id == event.id }
                if (index != -1) {
                    events[index] = events[index].copy(
                        title = newTitle,
                        date = newDate,
                        location = newLocation,
                        status = newStatus
                        // time & description dibiarkan sama
                    )
                }
                editingEvent = null
            }
        )
    }
}

@Composable
fun EventItem(
    event: Event,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }    // klik body → ke detail
    ) {
        Text(text = event.title, style = MaterialTheme.typography.titleMedium)
        Text("Tanggal: ${event.date}")
        Text("Lokasi: ${event.location}")
        Text("Status: ${event.status}")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onEdit) {
                Text("Edit")
            }
            TextButton(onClick = onDelete) {
                Text("Hapus")
            }
        }
    }
}

@Composable
private fun EventDialog(
    title: String,
    initialEvent: Event?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var titleText by remember { mutableStateOf(initialEvent?.title ?: "") }
    var dateText by remember { mutableStateOf(initialEvent?.date ?: "") }
    var locationText by remember { mutableStateOf(initialEvent?.location ?: "") }
    var statusText by remember { mutableStateOf(initialEvent?.status ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text("Judul") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("Tanggal (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = locationText,
                    onValueChange = { locationText = it },
                    label = { Text("Lokasi") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = statusText,
                    onValueChange = { statusText = it },
                    label = { Text("Status") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (titleText.isNotBlank() && dateText.isNotBlank()) {
                    onConfirm(titleText, dateText, locationText, statusText)
                }
            }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
