package com.example.thandroid.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thandroid.R
import com.example.thandroid.model.Statistic

class StatisticRvAdapter(private var statistics : List<Statistic>) : RecyclerView.Adapter<StatisticRvAdapter.StatisticViewHolder>() {
    class StatisticViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.tvName)
        val amount = view.findViewById<TextView>(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        val view = View.inflate(parent.context, R.layout.statistics_rv_item, null)
        return StatisticViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(statistics.isEmpty())
            return 0
        return statistics.size
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, position: Int) {
        holder.name.text = statistics[position].name
        holder.amount.text = statistics[position].amount.toString()
    }

    fun submitList(newStatistics: List<Statistic>) {
        statistics = newStatistics
        notifyDataSetChanged()
    }
}