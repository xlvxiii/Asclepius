package com.dicoding.asclepius.view.news

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemNewsBinding

class NewsAdapter(private val onClickReadButton: (ArticlesItem) -> Unit) : ListAdapter<ArticlesItem, NewsAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

//    private lateinit var onItemClickCallback: OnItemClickCallback

    class ArticleViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(news: ArticlesItem) {
            Glide.with(binding.root.context).load(news.urlToImage)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.baseline_broken_image_24)
                        .error(R.drawable.baseline_broken_image_24))
                .into(binding.thumbnail)
            binding.apply {
                title.text = news.title
                source.text = "Source: ${news.source?.name}"
                description.text = news.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)

//        holder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(news)
//        }

        val btnReadArticle = holder.binding.btnRead
        btnReadArticle.setOnClickListener {
            onClickReadButton(news)
        }
    }

//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }

//    interface OnItemClickCallback {
//        fun onItemClicked(data: ArticlesItem)
//    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}