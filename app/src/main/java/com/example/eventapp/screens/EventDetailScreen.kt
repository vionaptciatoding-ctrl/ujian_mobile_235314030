package com.example.eventapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eventapp.data.model.Event
import com.example.eventapp.data.remote.ApiClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    id: Int,
    onBack: () -> Unit
) {
    var event by remember { mutableStateOf<Event?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // CASE 2 — GET EVENT BY ID
    LaunchedEffect(id) {
        try {
            val response = ApiClient.eventService.getEventById(id = id)
            if (response.isSuccessful) {
                event = response.body()?.data
            } else {
                errorMessage = "Error ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = e.localizedMessage ?: "Unknown error"
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Detail Event") },
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
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(20.dp)
                )
            }

            errorMessage.isNotEmpty() -> {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                )
            }

            event != null -> {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    Text(
                        text = event!!.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(12.dp))

                    Text("Tanggal: ${event!!.date}")
                    Text("Waktu  : ${event!!.time}")
                    Text("Lokasi : ${event!!.location}")
                    Text("Status : ${event!!.status}")
                    Spacer(Modifier.height(8.dp))
                    Text(event!!.description ?: "Tidak ada deskripsi")
                }
            }
        }
    }
}
