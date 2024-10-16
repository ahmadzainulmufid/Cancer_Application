package com.dicoding.asclepius.ui.history

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.database.History

class HistoryAdapter(private var histories: List<History>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.history_image)
        val resultTextView: TextView = itemView.findViewById(R.id.history_result)
        val dateTextView: TextView = itemView.findViewById(R.id.history_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = histories[position]
        holder.imageView.setImageDrawable(null)
        holder.imageView.setImageURI(Uri.parse(history.imageUri))
        holder.resultTextView.text = history.result
        holder.dateTextView.text = history.date
    }


    override fun getItemCount(): Int = histories.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newHistories: List<History>) {
        histories = newHistories
        notifyDataSetChanged()
    }
}
