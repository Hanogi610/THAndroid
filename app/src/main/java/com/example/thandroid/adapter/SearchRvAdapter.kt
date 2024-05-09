package com.example.thandroid.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thandroid.R
import com.example.thandroid.model.Work

class SearchRvAdapter (private var works : List<Work>) : RecyclerView.Adapter<SearchRvAdapter.SearchRvViewHolder>() {
    class SearchRvViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.tvName)
        val editBtn = view.findViewById<ImageButton>(R.id.editButton)
        val deleteBtn = view.findViewById<ImageButton>(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRvViewHolder {
        val view = View.inflate(parent.context, R.layout.list_rv_item_view, null)
        return SearchRvViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(works.isEmpty())
            return 0
        return works.size
    }

    override fun onBindViewHolder(holder: SearchRvViewHolder, position: Int) {
        holder.name.text = works[position].name
        holder.editBtn.visibility = View.GONE
        holder.deleteBtn.visibility = View.GONE
    }

    fun submitList(newWorks: List<Work>) {
        works = newWorks
        notifyDataSetChanged()
    }
}