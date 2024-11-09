package com.dicoding.asclepius.data.safeargs

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictionResult(
    val label: String,
    val confidenceScore: Float,
    val uri: Uri?
) : Parcelable
