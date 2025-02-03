package com.example.foodplanner.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .build()
    val retrofitService = retrofit.create(RetrofitService::class.java)
}