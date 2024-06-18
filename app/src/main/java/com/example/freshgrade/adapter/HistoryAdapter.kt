package com.example.freshgrade.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freshgrade.data.response.HistoryResponseItem
import com.example.freshgrade.databinding.ItemViewHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(private var historyList: List<HistoryResponseItem>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemViewHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(historyItem: HistoryResponseItem) {
            Glide.with(binding.imageVewFruit.context)
                .load(historyItem.imageUrl)
                .into(binding.imageVewFruit)

            binding.nameStory.text = historyItem.fruit
            binding.fruitFreshness.text = "Freshness: ${historyItem.value} %"
            binding.fruitDisease.text = "Disease: ${historyItem.disease}"
            binding.dateFruit.text = formatDate(historyItem.createdAt)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newHistoryList: List<HistoryResponseItem>) {
        historyList = newHistoryList
        notifyDataSetChanged()
    }

    private fun formatDate(dateString: String): String {
        return try {
            // Adjust the parsing format according to your date string format
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date: Date = inputFormat.parse(dateString) ?: Date()
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            dateString // Return the original string in case of an error
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size
}