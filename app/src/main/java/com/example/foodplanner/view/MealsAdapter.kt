package com.example.foodplanner.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.foodplanner.R
import com.example.foodplanner.model.Meal

class MealsAdapter(
    val context: Context,
    var data: List<Meal>,
) : RecyclerView.
Adapter<MealsAdapter.MealsHolder>() {
    class MealsHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val mealName: TextView = row.findViewById(R.id.mealName)
        val mealImage: ImageView = row.findViewById(R.id.mealImage)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_meal_card, parent, false)
        val mealsHolder = MealsHolder(layout)
        return mealsHolder
    }


    override fun onBindViewHolder(holder: MealsHolder, position: Int) {
        holder.mealName.text = data[position].strMeal.toString()
        Glide.with(context)
            .load(data[position].strMealThumb)
            .transform(RoundedCorners(25))
            .into(holder.mealImage)

        holder.row.setOnClickListener {
//            Toast.makeText(context, data[position], Toast.LENGTH_SHORT).show()
        }
    }

}
