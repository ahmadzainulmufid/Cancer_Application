package com.dicoding.asclepius

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.information.ArticlesItem
import com.dicoding.asclepius.databinding.ItemInformationBinding

class InformationAdapter(private val listInformation: List<ArticlesItem>) :
    RecyclerView.Adapter<InformationAdapter.InformationViewHolder>() {

    inner class InformationViewHolder(private val binding: ItemInformationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(information: ArticlesItem) {
            binding.tvInformationName.text = information.title ?: "No title available"

            val description = information.description as? String
            binding.tvInformationDescription.text = description ?: "No description available"

            val imageUrl = information.urlToImage as? String
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(binding.ivInformationImage.context)
                    .load(imageUrl)
                    .into(binding.ivInformationImage)
                binding.ivInformationImage.visibility = View.VISIBLE
            } else {
                binding.ivInformationImage.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationViewHolder {
        val binding = ItemInformationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InformationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InformationViewHolder, position: Int) {
        holder.bind(listInformation[position])
    }

    override fun getItemCount(): Int = listInformation.size
}
