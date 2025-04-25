package com.example.foodplanner.core.utils

import android.util.Log
import com.example.foodplanner.db.MealDao
import com.example.foodplanner.model.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun checkIfFavorite(meal: Meal, dao: MealDao, uid: String): Boolean {
    try {
        val result = withContext(Dispatchers.IO) {
            dao.getMealById(meal.idMeal.toInt(), uid)
        }
        meal.isFavorite = result != null
        return result != null
    } catch (e: Exception) {
        Log.i("FavoriteUtils", "com.example.foodplanner.core.utils.checkIfFavorite: ${e.message}")
        return false
    }
}