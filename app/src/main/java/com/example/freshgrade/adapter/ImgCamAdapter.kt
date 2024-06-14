package com.example.freshgrade.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freshgrade.data.carousel_item.ImgCamera
import com.example.freshgrade.databinding.ItemViewCarouselCameraBinding

class ImgCamAdapter(private var items: List<ImgCamera>) :
    RecyclerView.Adapter<ImgCamAdapter.CarouselViewHolder>() {

    class CarouselViewHolder(val binding: ItemViewCarouselCameraBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemViewCarouselCameraBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = items[position]
        holder.binding.profileImageView.setImageResource(item.imageId)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(newItems: List<ImgCamera>) {
        items = newItems
        notifyDataSetChanged()
    }
}