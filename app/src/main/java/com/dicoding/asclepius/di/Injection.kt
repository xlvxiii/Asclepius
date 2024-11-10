package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.repositories.NewsRepository
import com.dicoding.asclepius.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        return NewsRepository.getInstance(apiService)
    }
}