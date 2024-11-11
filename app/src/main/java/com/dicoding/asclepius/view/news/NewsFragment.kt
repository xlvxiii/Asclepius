package com.dicoding.asclepius.view.news

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.repositories.Result
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance()
        val viewModel: NewsViewModel by viewModels {
            factory
        }

        binding?.rvNews?.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        viewModel.searchNews("cancer", "health").observe(viewLifecycleOwner) { newsList ->
            if (newsList != null) {
                when(newsList) {
                    is Result.Loading -> {
                        binding?.shimmerLayout?.startShimmer()
                    }
                    is Result.Success -> {
                        setNewsData(newsList.data?.filterNot { it?.title?.contains("[removed]", true) ?: false })
                    }
                    is Result.Error -> {
                        binding?.shimmerLayout?.apply {
                            stopShimmer()
                            visibility = View.GONE
                        }
                        Log.e("NewsFragment", "Error: ${newsList.error}")
                        Toast.makeText(requireActivity(), "Failed to load data: ${newsList.error}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setNewsData(newsList: List<ArticlesItem?>?) {
        val adapter = NewsAdapter {
            if (it.url != null) {
                redirectToArticle(it.url)
            }
        }

        adapter.submitList(newsList)
        binding?.rvNews?.adapter = adapter

        binding?.shimmerLayout?.apply {
            stopShimmer()
            visibility = View.GONE
        }
    }

    private fun redirectToArticle(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}