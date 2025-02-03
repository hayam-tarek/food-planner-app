package com.example.foodplanner.network

import com.example.foodplanner.model.MealModel
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("random.php")
    suspend fun randomMeal(): MealModel

    @GET("lookup.php")
    suspend fun mealDetails(@Query("i") mealId: String): MealModel
}