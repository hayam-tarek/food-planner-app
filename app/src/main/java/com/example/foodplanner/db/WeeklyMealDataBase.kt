package com.example.foodplanner.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodplanner.model.WeeklyMeal

@Database(entities = [WeeklyMeal::class], version = 2)
abstract class WeeklyMealDataBase : RoomDatabase() {
    abstract fun weeklyMealDao(): WeeklyMealDao

    companion object {
        const val DATABASE_NAME = "weekly_meal_database"

        @Volatile
        private var INSTANCE: WeeklyMealDataBase? = null
        fun getInstance(context: android.content.Context): WeeklyMealDataBase {
            return INSTANCE ?: synchronized(this) {
                val tempInstance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    WeeklyMealDataBase::class.java, DATABASE_NAME
                ).build()
                INSTANCE = tempInstance
                tempInstance
            }
        }
    }
}