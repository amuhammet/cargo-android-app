package com.example.cargo.ui.tracking

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cargo.data.model.Load

class LoadAdapter(private var loads: List<Load>) : RecyclerView.Adapter<LoadAdapter.LoadViewHolder>() {
    class LoadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInfo: TextView = view.findViewById(android.R.id.text1)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = 
        LoadViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))

    override fun onBindViewHolder(holder: LoadViewHolder, position: Int) {
        val load = loads[position]
        holder.tvInfo.text = "${load.trackingNumber} - ${load.status}"
    }
    override fun getItemCount() = loads.size
    fun updateData(newLoads: List<Load>) { loads = newLoads; notifyDataSetChanged() }
}
