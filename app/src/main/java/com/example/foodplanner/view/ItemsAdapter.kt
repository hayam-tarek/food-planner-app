package com.example.foodplanner.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.foodplanner.R

class ItemsAdapter(val context: Context, private val data: List<String>) :
    Adapter<ItemsAdapter.ItemsHolder>() {
    class ItemsHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val title: TextView = row.findViewById(R.id.textBoxTitle)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_text_box, parent, false)
        val itemsHolder = ItemsHolder(layout)
        return itemsHolder
    }


    override fun onBindViewHolder(holder: ItemsHolder, position: Int) {
        holder.title.text = data[position].toString()
        holder.row.setOnClickListener {
//            Toast.makeText(context, data[position], Toast.LENGTH_SHORT).show()
        }
    }

}