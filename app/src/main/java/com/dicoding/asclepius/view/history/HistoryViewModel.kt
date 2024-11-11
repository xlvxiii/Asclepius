package com.dicoding.asclepius.view.history

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.repositories.HistoryRepository

class HistoryViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    fun getHistory() = historyRepository.getHistory()
}