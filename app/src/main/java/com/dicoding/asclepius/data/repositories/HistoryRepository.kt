package com.dicoding.asclepius.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.entity.PredictionResultEntity
import com.dicoding.asclepius.data.local.room.HistoryDao

class HistoryRepository private constructor(private val historyDao: HistoryDao){

    suspend fun savePredictionResult(label: String, confidenceScore: Float, uri: String) {
        try {
            val predictionResultEntity = PredictionResultEntity(
                label = label,
                confidenceScore = confidenceScore,
                uri = uri
            )

            historyDao.insertPredictionResult(predictionResultEntity)
        } catch (e: Exception) {
            Log.e("HistoryRepository", "Error saving prediction result", e)
        }
    }

    fun getHistory(): LiveData<List<PredictionResultEntity>> {
        return historyDao.getHistory()
    }

    companion object {
        @Volatile
        private var instance: HistoryRepository? = null
        fun getInstance(
            historyDao: HistoryDao,
        ): HistoryRepository =
            instance ?: synchronized(this) {
                instance ?: HistoryRepository(historyDao)
            }.also { instance = it }
    }
}