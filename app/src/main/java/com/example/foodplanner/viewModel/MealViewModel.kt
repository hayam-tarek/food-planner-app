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
    val randomMeal: LiveData<MealModel>
        get() = _randomMeal
    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message
    private val _mealDetails = MutableLiveData<MealModel>()
    val mealDetails: LiveData<MealModel>
        get() = _mealDetails

    init {

    }

    fun getRandomMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = retrofit.randomMeal()
                if (meal.meals.isEmpty()) {
                    _message.postValue("No meal found")
                } else {
                    _randomMeal.postValue(meal)
                }
            } catch (e: Exception) {
                Log.i("MealViewModel", "getRandomMeal: ${e.message}")
            }
        }
    }

    fun getMealDetails(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = retrofit.mealDetails(mealId)
                if (meal.meals.isEmpty()) {
                    _message.postValue("No meal found")
                } else {
                    _mealDetails.postValue(meal)
                }
            } catch (e: Exception) {
                Log.i("MealViewModel", "getMealDetails: ${e.message}")
            }
        }
    }

    fun addMealToFav(meal: Meal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = dao.insert(meal)
                if (result == -1L) {
                    _message.postValue("Meal already exists in the favorites")
                } else {
                    _message.postValue("Meal added to the favorites")
                }
            } catch (e: Exception) {
                Log.i("MealViewModel", "addMealToDb: ${e.message}")
            }
        }
    }

    fun deleteMealFromFav(meal: Meal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.delete(meal)
                _message.postValue("Meal deleted from the favorites")
            } catch (e: Exception) {
                Log.i("MealViewModel", "deleteMealFromFav: ${e.message}")
            }
        }
    }
}