package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.PredictionResultEntity

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPredictionResult(predictionResult: PredictionResultEntity)

    @Query("SELECT * FROM prediction_results")
    fun getHistory(): LiveData<List<PredictionResultEntity>>
}