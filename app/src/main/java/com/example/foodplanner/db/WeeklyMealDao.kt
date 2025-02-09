package com.example.foodplanner.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodplanner.model.WeeklyMeal

@Dao
interface WeeklyMealDao {

    @Query("SELECT * FROM weekly_meals WHERE dayOfWeek = :day")
    suspend fun getMealByDay(day: String): WeeklyMeal

    @Query("SELECT * FROM weekly_meals WHERE mealId = :id")
    suspend fun getMealById(id: String): WeeklyMeal

    @Query("SELECT * FROM weekly_meals")
    suspend fun getAllMeals(): List<WeeklyMeal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: WeeklyMeal)

    @Delete
    suspend fun deleteMeal(meal: WeeklyMeal)

    @Query("DELETE FROM weekly_meals")
    suspend fun deleteAllMeals()
}