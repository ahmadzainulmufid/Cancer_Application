package com.dicoding.asclepius.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        adapter = HistoryAdapter(emptyList())

        binding.history.layoutManager = LinearLayoutManager(context)
        binding.history.adapter = adapter

        historyViewModel.getHistory().observe(viewLifecycleOwner) { historyList ->
            historyList.forEach { Log.d("DatabaseURI", "Saved URI: ${it.imageUri}") }
            adapter.updateData(historyList)
            binding.progressBar.visibility = View.GONE
        }

        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
