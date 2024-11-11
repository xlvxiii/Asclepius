package com.dicoding.asclepius.view.history

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.asclepius.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var _binding: FragmentHistoryBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HistoryViewModel by viewModels { factory }

        val historyAdapter = HistoryAdapter()

        viewModel.getHistory().observe(viewLifecycleOwner) { predictionResults ->
            historyAdapter.submitList(predictionResults)
        }

        binding.rvHistory.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = historyAdapter
        }
    }
}