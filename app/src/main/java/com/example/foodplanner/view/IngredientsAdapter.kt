package com.example.foodplanner.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.R

class IngredientsAdapter(
    val context: Context,
    var data: List<String>,
    var subData: List<String>
) : RecyclerView.
Adapter<IngredientsAdapter.ItemsHolder>() {
    class ItemsHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val title: TextView = row.findViewById(R.id.itemTitle)
        val subTitle: TextView = row.findViewById(R.id.itemSubTitle)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsHolder {
        val layout =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_ingredient_item, parent, false)
        val itemsHolder = ItemsHolder(layout)
        return itemsHolder
    }


    override fun onBindViewHolder(holder: ItemsHolder, position: Int) {
        holder.title.text = data[position]
        holder.subTitle.text = subData[position]
        holder.row.setOnClickListener {
            Toast.makeText(context, data[position], Toast.LENGTH_LONG).show()
        }
    }

}