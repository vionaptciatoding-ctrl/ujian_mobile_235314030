package com.example.eventapp.domain

import com.example.eventapp.data.model.Event

fun getEventById(events: List<Event>, id: Int): Event? {
    return events.find { it.id == id }
}
