package com.example.foodplanner.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodplanner.model.Meal

@Database(entities = [Meal::class], version = 3)
abstract class MealDataBase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object {
        private const val DATABASE_NAME = "meal_database"

        @Volatile
        private var INSTANCE: MealDataBase? = null
        fun getInstance(context: android.content.Context): MealDataBase {
            return INSTANCE ?: synchronized(this) {
                val tempInstance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    MealDataBase::class.java, DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = tempInstance
                tempInstance
            }
        }
    }
}