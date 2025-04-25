package com.example.foodplanner.features.filterMeals.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.foodplanner.R
import com.example.foodplanner.model.Meal
import com.example.foodplanner.utils.MealListener

class MealsAdapter(
    val context: Context,
    var data: List<Meal>,
    var mealListener: MealListener
) : RecyclerView.
Adapter<MealsAdapter.MealsHolder>() {
    class MealsHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val mealName: TextView = row.findViewById(R.id.mealName)
        val mealImage: ImageView = row.findViewById(R.id.mealImage)
        val mealFavIcon: ImageView = row.findViewById(R.id.favIcon)
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
        val meal = data[position]
        holder.mealName.text = meal.strMeal.toString()
        Glide.with(context)
            .load(meal.strMealThumb)
            .transform(RoundedCorners(25))
            .placeholder(R.drawable.loading)
            .into(holder.mealImage)

        updateFavoriteIcon(holder.mealFavIcon, meal.isFavorite)

        holder.row.setOnClickListener {
            mealListener.onMealClicked(meal)
        }

        holder.mealFavIcon.setOnClickListener {
            mealListener.onMealFavClicked(meal)
//            meal.isFavorite = !meal.isFavorite
//            updateFavoriteIcon(holder.mealFavIcon, meal.isFavorite)
        }
    }

    private fun updateFavoriteIcon(icon: ImageView, isFavorite: Boolean) {
        if (isFavorite) {
            icon.setImageResource(R.drawable.in_fav)
            icon.setColorFilter(ContextCompat.getColor(icon.context, android.R.color.holo_red_dark))
        } else {
            icon.setImageResource(R.drawable.out_fav)
            icon.setColorFilter(ContextCompat.getColor(icon.context, android.R.color.darker_gray))
        }
    }

}
