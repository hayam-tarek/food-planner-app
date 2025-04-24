package com.example.foodplanner.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "weekly_meals")
data class WeeklyMeal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mealId: String,
    val uid: String,
    val mealName: String,
    val mealCategory: String,
    val dayOfWeek: String,
    val dayShort: String,
    val imageUrl: String? = null,
    val mealJson: String
)

fun convertMealToJson(meal: Meal): String {
    val gson = Gson()
    return gson.toJson(meal)
}

fun convertJsonToMeal(json: String): Meal {
    val gson = Gson()
    return gson.fromJson(json, Meal::class.java)
}