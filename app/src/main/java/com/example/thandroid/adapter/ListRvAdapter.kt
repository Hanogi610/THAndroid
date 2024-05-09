package com.example.thandroid.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thandroid.R
import com.example.thandroid.model.Work

class ListRvAdapter(
    private var works : List<Work>,
    private val editClick: (Work) -> Unit,
    private val deleteClick: (Work) -> Unit
) : RecyclerView.Adapter<ListRvAdapter.ListRvViewHolder>() {
    class ListRvViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.tvName)
        val editBtn = view.findViewById<ImageButton>(R.id.editButton)
        val deleteBtn = view.findViewById<ImageButton>(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRvViewHolder {
        val view = View.inflate(parent.context, R.layout.list_rv_item_view, null)
        return ListRvViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(works.isEmpty())
            return 0
        return works.size
    }

    override fun onBindViewHolder(holder: ListRvViewHolder, position: Int) {
        holder.name.text = works[position].name + " - " + works[position].status
        holder.editBtn.setOnClickListener {
            editClick(works[position])
        }
        holder.deleteBtn.setOnClickListener {
            deleteClick(works[position])
        }
    }

    fun submitList(newWorks: List<Work>) {
        works = newWorks
        notifyDataSetChanged()
    }
}