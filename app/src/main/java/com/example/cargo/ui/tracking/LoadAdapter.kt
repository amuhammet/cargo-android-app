package com.example.cargo.ui.tracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cargo.data.model.Load

class LoadAdapter(private var loads: List<Load>) : RecyclerView.Adapter<LoadAdapter.LoadViewHolder>() {
    class LoadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInfo: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return LoadViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LoadViewHolder, position: Int) {
        val load = loads[position]
        holder.tvInfo.text = "${load.id} - ${load.status}"
    }

    override fun getItemCount(): Int = loads.size

    fun updateData(newLoads: List<Load>) {
        loads = newLoads
        notifyDataSetChanged()
    }
}
