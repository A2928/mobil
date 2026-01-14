package com.example.proj.api

import com.example.proj.data.BooksResponse
import com.example.proj.data.WorkDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenLibraryApi {
    @GET("subjects/fiction.json?limit=20")
    suspend fun getFictionBooks(): BooksResponse
    
    @GET("works/{workId}.json")
    suspend fun getWorkDetail(@Path("workId") workId: String): com.example.proj.data.WorkDetailResponse
}
