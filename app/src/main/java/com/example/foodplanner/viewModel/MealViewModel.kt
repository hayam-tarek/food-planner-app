package com.example.foodplanner.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.db.MealDao
import com.example.foodplanner.model.Meal
import com.example.foodplanner.model.MealModel
import com.example.foodplanner.network.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MealFactory(private val retrofit: RetrofitService, private val dao: MealDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            return MealViewModel(retrofit, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MealViewModel(private val retrofit: RetrofitService, private val dao: MealDao) : ViewModel() {
    private val _randomMeal = MutableLiveData<MealModel>()
    val randomMeal: LiveData<MealModel> get() = _randomMeal

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _mealDetails = MutableLiveData<MealModel>()
    val mealDetails: LiveData<MealModel> get() = _mealDetails

    private val _favorites = MutableLiveData<List<Meal>>()
    val favorites: LiveData<List<Meal>> get() = _favorites

    private val _favoriteMeal = MutableLiveData<Meal>()
    val favoriteMeal: LiveData<Meal> get() = _favoriteMeal

    init {

    }

    fun getRandomMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = retrofit.randomMeal()
                withContext(Dispatchers.Main) {
                    if (meal.meals.isEmpty()) {
                        _message.postValue("No meal found")
                    } else {
                        _randomMeal.postValue(meal)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = "Error fetching meal: ${e.message}"
                }
                Log.i("MealViewModel", "getRandomMeal: ${e.message}")
            }
        }
    }

    fun getMealDetails(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = retrofit.mealDetails(mealId)
                withContext(Dispatchers.Main) {
                    if (meal.meals.isEmpty()) {
                        _message.postValue("No meal found")
                    } else {
                        _mealDetails.postValue(meal)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = "Error fetching meal: ${e.message}"
                }
                Log.i("MealViewModel", "getMealDetails: ${e.message}")
            }
        }
    }

    fun addMealToFav(meal: Meal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = dao.insert(meal)
                withContext(Dispatchers.Main) {
                    if (result == -1L) {
                        _message.postValue("Meal already exists in the favorites")
                    } else {
                        _message.postValue("Meal added to the favorites")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = "Error adding meal to favorites: ${e.message}"
                }
                Log.i("MealViewModel", "addMealToDb: ${e.message}")
            }
        }
    }

    fun deleteMealFromFav(meal: Meal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.delete(meal)
                withContext(Dispatchers.Main) {
                    _message.postValue("Meal deleted from the favorites")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = "Error deleting meal from favorites: ${e.message}"
                }
                Log.i("MealViewModel", "deleteMealFromFav: ${e.message}")
            }
        }
    }

    fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meals = dao.getAll()
                withContext(Dispatchers.Main) {
                    if (meals.isEmpty()) {
                        _message.postValue("No favorites found")
                    } else {
                        _favorites.postValue(meals)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = "Error fetching favorites: ${e.message}"
                }
                Log.i("MealViewModel", "getFavorites: ${e.message}")
            }
        }
    }

    fun getFavoriteMealById(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = dao.getMealById(mealId.toInt())
                withContext(Dispatchers.Main) {
                    if (meal == null) {
                        _message.postValue("No meal found")
                    } else {
                        _favoriteMeal.postValue(meal)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = "Error fetching meal: ${e.message}"
                }
                Log.i("MealViewModel", "getFavoriteMealById: ${e.message}")
            }
        }
    }
}