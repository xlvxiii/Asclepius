package com.dicoding.asclepius.view.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.local.entity.PredictionResultEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import kotlin.math.round

class HistoryAdapter : ListAdapter<PredictionResultEntity, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {
    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(predictionResult: PredictionResultEntity) {
            binding.apply {
                imgResult.setImageURI(predictionResult.uri.toUri())
                tvLabel.text = "Hasil analisis: ${predictionResult.label}"
                tvConfidenceScore.text = "Confidence score: ${round(predictionResult.confidenceScore*100)}%"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val predictionResult = getItem(position)
        holder.bind(predictionResult)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PredictionResultEntity> =
            object : DiffUtil.ItemCallback<PredictionResultEntity>() {
                override fun areItemsTheSame(oldHistory: PredictionResultEntity, newHistory: PredictionResultEntity): Boolean {
                    return oldHistory.id == newHistory.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: PredictionResultEntity,
                    newItem: PredictionResultEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}