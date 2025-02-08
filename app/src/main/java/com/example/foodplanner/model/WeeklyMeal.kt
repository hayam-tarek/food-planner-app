package com.example.foodplanner.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_meals")
data class WeeklyMeal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mealId: String,
    val mealName: String,
    val mealCategory: String,
    val dayOfWeek: String,
    val dayShort: String,
    val imageUrl: String? = null
)