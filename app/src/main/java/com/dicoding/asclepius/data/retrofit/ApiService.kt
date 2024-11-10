package com.dicoding.asclepius.data.retrofit

import com.dicoding.asclepius.data.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines")
    suspend fun searchNews(@Query("q") query: String, @Query("category") category: String, @Query("language") language: String, @Query("apiKey") apiKey: String): NewsResponse
}