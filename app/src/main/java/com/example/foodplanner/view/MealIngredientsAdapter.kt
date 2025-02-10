package com.example.foodplanner.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.foodplanner.R

class MealIngredientsAdapter(
    val context: Context,
    var data: List<String>,
    var subData: List<String>
) : RecyclerView.
Adapter<MealIngredientsAdapter.ItemsHolder>() {
    class ItemsHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val title: TextView = row.findViewById(R.id.itemTitle)
        val subTitle: TextView = row.findViewById(R.id.itemSubTitle)
        val ingredientImage: ImageView = row.findViewById(R.id.ingredinentImage)
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
        Glide.with(context)
            .load("https://www.themealdb.com/images/ingredients/${data[position]}-Small.png")
            .transform(RoundedCorners(25))
            .placeholder(R.drawable.loading)
            .into(holder.ingredientImage)
        holder.row.setOnClickListener {
            Toast.makeText(context, data[position], Toast.LENGTH_LONG).show()
        }
    }

}