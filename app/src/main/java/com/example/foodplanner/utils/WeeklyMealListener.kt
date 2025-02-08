package com.example.foodplanner.utils

import com.example.foodplanner.model.WeeklyMeal

interface WeeklyMealListener {
    fun onWeeklyMealClicked(mealId: String)
    fun onDeleteIconClicked(weeklyMeal: WeeklyMeal)
}