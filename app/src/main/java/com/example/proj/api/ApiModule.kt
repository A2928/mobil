package com.example.proj.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {
    private const val BASE_URL = "https://openlibrary.org/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val api: OpenLibraryApi = retrofit.create(OpenLibraryApi::class.java)
}
