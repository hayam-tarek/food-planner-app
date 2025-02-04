package com.example.foodplanner.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.foodplanner.model.Meal

@Dao
interface MealDao {

    @Query("SELECT * FROM meals")
    suspend fun getAll(): Array<Meal>

    @Query("SELECT * FROM meals WHERE idMeal = :id")
    suspend fun getMealById(id: Int): List<Meal>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meal): Long

    @Update
    suspend fun update(meal: Meal): Int

    @Delete
    suspend fun delete(meal: Meal)
}