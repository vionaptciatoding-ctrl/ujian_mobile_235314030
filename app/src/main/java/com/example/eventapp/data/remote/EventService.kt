package com.example.eventapp.data.remote

import com.example.eventapp.data.model.ApiResponse
import com.example.eventapp.data.model.Event
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EventService {

    // GET ALL
    @GET("event-api-php.php")
    suspend fun getAllEvents(
        @Query("action") action: String = "getAll"
    ): Response<ApiResponse<List<Event>>>

    // GET BY ID
    @GET("event-api-php.php")
    suspend fun getEventById(
        @Query("action") action: String = "getById",
        @Query("id") id: Int
    ): Response<ApiResponse<Event>>

    // GET BY DATE
    @GET("event-api-php.php")
    suspend fun getEventByDate(
        @Query("action") action: String = "getByDate",
        @Query("date") date: String
    ): Response<ApiResponse<List<Event>>>
}