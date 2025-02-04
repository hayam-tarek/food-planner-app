package com.example.foodplanner.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.model.MealModel
import com.example.foodplanner.network.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MealFactory(private val retrofit: RetrofitService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            return MealViewModel(retrofit) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MealViewModel(private val retrofit: RetrofitService) : ViewModel() {
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
}