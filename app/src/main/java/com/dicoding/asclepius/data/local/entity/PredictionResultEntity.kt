package com.dicoding.asclepius.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prediction_results")
class PredictionResultEntity(
    @PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    val id: Int = 0,

    @field:ColumnInfo(name = "label")
    val label: String,

    @field:ColumnInfo(name = "confidence_score")
    val confidenceScore: Float,

    @field:ColumnInfo(name = "uri")
    val uri: String
)