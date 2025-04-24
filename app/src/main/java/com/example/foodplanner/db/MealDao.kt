package com.example.foodplanner.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.foodplanner.model.Meal

@Dao
interface MealDao {

    @Query("SELECT * FROM meals WHERE uid = :userId")
    suspend fun getAll(userId: String): List<Meal>

    @Query("SELECT * FROM meals WHERE idMeal = :id AND uid = :userId")
    suspend fun getMealById(id: Int, userId: String): Meal

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meal): Long

    @Update
    suspend fun update(meal: Meal): Int

    @Query("DELETE FROM meals WHERE idMeal = :mealId AND uid = :userId")
    suspend fun delete(mealId: String, userId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM meals WHERE idMeal = :mealId AND uid = :userId)")
    suspend fun isMealFavorite(mealId: String, userId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(meals: List<Meal>): List<Long>

    @Query("DELETE FROM meals WHERE uid = :userId")
    suspend fun deleteAll(userId: String)
}