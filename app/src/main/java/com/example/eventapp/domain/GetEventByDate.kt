package com.example.eventapp.domain

import com.example.eventapp.data.model.Event
import com.example.eventapp.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetEventByDate {

    suspend operator fun invoke(date: String): List<Event> {
        return withContext(Dispatchers.IO) {
            val response = ApiClient.eventService.getEventByDate(date = date)

            if (response.isSuccessful) {
                response.body()?.data ?: emptyList()
            } else {
                emptyList()
            }
        }
    }
}
