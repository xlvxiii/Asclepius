package com.dicoding.asclepius.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.retrofit.ApiService

class NewsRepository private constructor(
    private val apiService: ApiService
) {
    private val _listNews = MutableLiveData<List<ArticlesItem?>?>()
    private val listNews: LiveData<Result<List<ArticlesItem?>?>> = _listNews.map { Result.Success(it) }

    fun searchNews(
        q: String, category: String, language: String = "en", apiKey: String = BuildConfig.API_KEY
    ): LiveData<Result<List<ArticlesItem?>?>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.searchNews(q, category, language, apiKey)
            val articles = response.articles
            _listNews.value = articles
            emit(Result.Success(articles))
        } catch (e: Exception) {
            Log.d("NewsRepository", "searchNews: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }

        emitSource(listNews)
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            apiService: ApiService
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService)
            }.also { instance = it }
    }
}