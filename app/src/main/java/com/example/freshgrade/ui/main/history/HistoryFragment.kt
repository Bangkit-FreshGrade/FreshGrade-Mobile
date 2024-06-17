package com.example.freshgrade.ui.main.history

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freshgrade.R
import com.example.freshgrade.adapter.HistoryAdapter
import com.example.freshgrade.databinding.FragmentHistoryBinding
import com.example.freshgrade.databinding.FragmentProfileBinding
import com.example.freshgrade.di.Injection
import com.example.freshgrade.ui.main.profile.ProfileViewModel
import com.example.freshgrade.ui.util.ViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel: HistoryViewModel by viewModels {
        ViewModelFactory(Injection.provideRepository(requireContext()))
    }

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.historyRv.layoutManager = LinearLayoutManager(context)
        historyAdapter = HistoryAdapter(emptyList())
        binding.historyRv.adapter = historyAdapter

        historyViewModel.history.observe(viewLifecycleOwner) { historyItems ->
            if (historyItems != null) {
                historyAdapter.updateData(historyItems)
            }
        }

        historyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        historyViewModel.fetchHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}