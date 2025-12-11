package com.example.eventapp.data.model

data class Event(
    val id: Int,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val description: String,
    val capacity: Int,
    val status: String
)

