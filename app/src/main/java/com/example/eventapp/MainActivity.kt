package com.example.eventapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.eventapp.screens.EventListScreen
import com.example.eventapp.screens.EventDetailScreen
import com.example.eventapp.screens.EventByDateScreen
import com.example.eventapp.ui.theme.EventAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EventAppTheme {

                // --------- STATE NAVIGASI ----------
                var selectedId by remember { mutableStateOf<Int?>(null) }
                var selectedDate by remember { mutableStateOf<String?>(null) }

                Scaffold { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {

                        when {
                            // CASE 1 — LIST EVENT (default)
                            selectedId == null && selectedDate == null -> {
                                EventListScreen(
                                    onEventClick = { id ->
                                        selectedId = id
                                    },
                                    onFilterClick = { date ->
                                        selectedDate = date
                                    }
                                )
                            }

                            // CASE 2 — DETAIL EVENT BY ID
                            selectedId != null -> {
                                EventDetailScreen(
                                    id = selectedId!!,
                                    onBack = { selectedId = null }
                                )
                            }

                            // CASE 3 — EVENT BY DATE
                            selectedDate != null -> {
                                EventByDateScreen(
                                    date = selectedDate!!,
                                    onBack = { selectedDate = null }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
