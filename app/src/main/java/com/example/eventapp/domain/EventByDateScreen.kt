package com.example.eventapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eventapp.data.model.Event
import com.example.eventapp.domain.GetEventByDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventByDateScreen(
    date: String,
    onBack: () -> Unit
) {
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // CASE 3 — GET EVENT BY DATE (pakai domain)
    LaunchedEffect(date) {
        try {
            val result = GetEventByDate().invoke(date)
            events = result
        } catch (e: Exception) {
            errorMessage = e.localizedMessage ?: "Error fetching data"
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Events on $date") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                errorMessage.isNotEmpty() -> {
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                events.isEmpty() -> {
                    Text("Tidak ada event pada tanggal ini.")
                }

                else -> {
                    LazyColumn {
                        items(events) { event ->
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text("Lokasi: ${event.location}")
                            Text("Status: ${event.status}")
                            Divider(Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
    }
}
