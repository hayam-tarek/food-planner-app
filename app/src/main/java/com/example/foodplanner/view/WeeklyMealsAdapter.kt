package com.example.foodplanner.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.R
import com.example.foodplanner.model.WeeklyMeal
import com.example.foodplanner.utils.WeeklyMealListener

class WeeklyMealsAdapter(
    val context: Context,
    var data: List<WeeklyMeal>,
    val listener: WeeklyMealListener
) : RecyclerView.
Adapter<WeeklyMealsAdapter.WeeklyMealsHolder>() {
    class WeeklyMealsHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val dayName: TextView = row.findViewById(R.id.dayName)
        val weeklyMealName: TextView = row.findViewById(R.id.weeklyMealName)
        val weeklyMealType: TextView = row.findViewById(R.id.weeklyMealType)

        //        val moreIcon: ImageView = row.findViewById(R.id.moreIcon)
        val deleteIcon: ImageView = row.findViewById(R.id.deleteIcon)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyMealsHolder {
        val layout =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_weekly_meal, parent, false)
        val areasHolder = WeeklyMealsHolder(layout)
        return areasHolder
    }


    override fun onBindViewHolder(holder: WeeklyMealsHolder, position: Int) {
        holder.dayName.text = data[position].dayShort
        holder.weeklyMealName.text = data[position].mealName
        holder.row.setOnClickListener {
            listener.onWeeklyMealClicked(data[position].mealId)
        }
        holder.deleteIcon.setOnClickListener {
            listener.onDeleteIconClicked(data[position])
        }
    }

}