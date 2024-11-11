package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.local.room.HistoryDatabase
import com.dicoding.asclepius.data.repositories.HistoryRepository
import com.dicoding.asclepius.data.repositories.NewsRepository
import com.dicoding.asclepius.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(): NewsRepository {
        val apiService = ApiConfig.getApiService()
        return NewsRepository.getInstance(apiService)
    }

    fun provideHistoryRepository(context: Context): HistoryRepository {
        val historyDao = HistoryDatabase.getInstance(context).historyDao()
        return HistoryRepository.getInstance(historyDao)
    }
}