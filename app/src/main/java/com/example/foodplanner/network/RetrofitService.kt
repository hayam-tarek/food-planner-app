package com.example.foodplanner.network

import com.example.foodplanner.model.AreaModel
import com.example.foodplanner.model.CategoryModel
import com.example.foodplanner.model.MealModel
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("random.php")
    suspend fun randomMeal(): MealModel

    @GET("lookup.php")
    suspend fun mealDetails(@Query("i") mealId: String): MealModel

    @GET("list.php?a=list")
    suspend fun areas(): AreaModel

    @GET("categories.php")
    suspend fun categories(): CategoryModel

    @GET("filter.php")
    suspend fun filterByCategory(@Query("c") categoryName: String): MealModel

    @GET("filter.php")
    suspend fun filterByArea(@Query("a") areaName: String): MealModel
}