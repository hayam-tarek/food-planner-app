package com.example.foodplanner.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.db.WeeklyMealDao
import com.example.foodplanner.model.Meal
import com.example.foodplanner.model.WeeklyMeal
import com.example.foodplanner.model.convertMealToJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class WeeklyMealFactory(private val dao: WeeklyMealDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeeklyMealViewModel::class.java)) {
            return WeeklyMealViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class WeeklyMealViewModel(private val dao: WeeklyMealDao) : ViewModel() {
    private val _weeklyMeals = MutableLiveData<List<WeeklyMeal>>()
    val weeklyMeals: LiveData<List<WeeklyMeal>> get() = _weeklyMeals

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _mealOfDay = MutableLiveData<WeeklyMeal>()
    val mealOfDay: LiveData<WeeklyMeal> get() = _mealOfDay

    private val _mealById = MutableLiveData<WeeklyMeal>()
    val mealById: LiveData<WeeklyMeal> get() = _mealById

    init {
        checkAndResetWeeklyMeals()
        getWeeklyMeals()
    }

    fun getWeeklyMeals() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meals = dao.getAllMeals()
                withContext(Dispatchers.Main) {
                    if (meals.isEmpty()) {
                        _weeklyMeals.value = listOf()
//                        _message.value = "No weekly meals found"
                    } else {
                        val sortedMeals = meals.sortedWith(compareBy {
                            when (it.dayShort) {
                                "Sun" -> 0
                                "Mon" -> 1
                                "Tue" -> 2
                                "Wed" -> 3
                                "Thu" -> 4
                                "Fri" -> 5
                                "Sat" -> 6
                                else -> 7
                            }
                        })
                        _weeklyMeals.value = sortedMeals
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = "Error: ${e.message}"
                }
                Log.i("WeeklyMealViewModel", "getWeeklyMeals: ${e.message}")
            }
        }
    }

    fun deleteMeal(weeklyMeal: WeeklyMeal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.deleteMeal(weeklyMeal)
                withContext(Dispatchers.Main) {
//                    _message.value = "Meal deleted successfully"
                }
                getWeeklyMeals()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = "Error: ${e.message}"
                }
                Log.i("WeeklyMealViewModel", "deleteMeal: ${e.message}")
            }
        }
    }

    fun insertMeal(meal: Meal, day: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingMeal = dao.getMealByDay(day)
                if (existingMeal == null) {
                    val mealJson = convertMealToJson(meal)
                    val weeklyMeal = WeeklyMeal(
                        mealName = meal.strMeal!!,
                        mealCategory = meal.strCategory!!,
                        mealId = meal.idMeal,
                        dayOfWeek = day,
                        dayShort = day.substring(0, 3),
                        imageUrl = meal.strMealThumb,
                        mealJson = mealJson
                    )
                    dao.insertMeal(weeklyMeal)
                    withContext(Dispatchers.Main) {
                        _message.value = "Meal added successfully"
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _message.value = "A meal already added for $day ⚠"
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = "Error: ${e.message}"
                }
                Log.i("WeeklyMealViewModel", "insertMeal: ${e.message}")
            }
        }
    }

    fun getMealByDay(day: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = dao.getMealByDay(day)
                withContext(Dispatchers.Main) {
                    if (meal == null) {
                        _message.value = "No meal found for $day"
                    } else {
                        _mealOfDay.value = meal
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = "Error: ${e.message}"
                }
                Log.i("WeeklyMealViewModel", "getMealByDay: ${e.message}")
            }
        }
    }

    fun getMealById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = dao.getMealById(id)
                withContext(Dispatchers.Main) {
                    if (meal == null) {
                        _message.value = "This meal is not in your weekly plan"
                    } else {
                        _mealById.value = meal
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = "Error: ${e.message}"
                }
                Log.i("WeeklyMealViewModel", "getMealById: ${e.message}")
            }
        }
    }

    fun checkAndResetWeeklyMeals() {
        viewModelScope.launch(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek == Calendar.SATURDAY && hourOfDay == 23 && minute == 59) {
                try {
                    dao.deleteAllMeals()
                    withContext(Dispatchers.Main) {
                        _weeklyMeals.value = listOf()
//                        _message.value = "Weekly plan has been reset."
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _message.value = "Error: ${e.message}"
                    }
                    Log.i("WeeklyMealViewModel", "checkAndResetWeeklyMeals: ${e.message}")
                }
            }
        }
    }
}