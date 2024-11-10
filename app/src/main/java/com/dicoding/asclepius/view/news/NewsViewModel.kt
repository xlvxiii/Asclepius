package com.dicoding.asclepius.view.news

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.repositories.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun searchNews(q: String, category: String, language: String = "en") = newsRepository.searchNews(q, category, language)
}