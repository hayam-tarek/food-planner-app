package com.example.foodplanner.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.R
import com.example.foodplanner.model.Area
import com.example.foodplanner.utils.AreaListener

class AreasAdapter(
    val context: Context,
    var data: List<Area>,
    val listener: AreaListener
) : RecyclerView.
Adapter<AreasAdapter.AreasHolder>() {
    class AreasHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val areaName: TextView = row.findViewById(R.id.areaName)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreasHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_area_item, parent, false)
        val areasHolder = AreasHolder(layout)
        return areasHolder
    }


    override fun onBindViewHolder(holder: AreasHolder, position: Int) {
        holder.areaName.text = data[position].strArea
        holder.row.setOnClickListener {
            listener.onAreaClicked(data[position].strArea)
        }
    }

}