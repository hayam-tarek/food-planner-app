package com.example.foodplanner.network

import com.example.foodplanner.model.MealModel
import retrofit2.http.GET

interface RetrofitService {
    @GET("random.php")
    suspend fun randomMeal(): MealModel
}