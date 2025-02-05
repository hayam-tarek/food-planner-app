package com.example.foodplanner.utils

import com.example.foodplanner.model.Meal

interface MealListener {
    fun onMealClicked(meal: Meal)
    fun onMealFavClicked(meal: Meal)
}