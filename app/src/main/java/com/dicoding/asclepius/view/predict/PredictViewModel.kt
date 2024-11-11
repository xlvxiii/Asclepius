package com.dicoding.asclepius.view.predict

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.repositories.HistoryRepository
import kotlinx.coroutines.launch

class PredictViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    var currentImageUri: Uri? = null

    fun savePredictionResult(label: String, confidenceScore: Float, uri: String) {
        viewModelScope.launch {
            historyRepository.savePredictionResult(label, confidenceScore, uri)
        }
    }
}