package com.example.eventapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    //private const val BASE_URL = "http://10.0.2.2/EventApp2/api/"

    //private const val BASE_URL = "http://192.168.110.109/EventApp2/api/"

    //private const val BASE_URL = "http://192.168.110.109:8000/api/"

    // private const val BASE_URL = "http://192.168.1.5:8000/api/"

    //private const val BASE_URL = "http://10.0.2.2/"

    //private const val BASE_URL = "http://10.0.2.2/event_api/"
    //
    //private const val BASE_URL = "http://192.168.1.10/event_api/"
    private const val BASE_URL = "http://10.30.208.107/event_api/"


    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val eventService: EventService = retrofit.create(EventService::class.java)
}
