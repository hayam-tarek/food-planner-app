package com.example.foodplanner.view.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.core.utils.checkIfFavorite
import com.example.foodplanner.db.MealDao
import com.example.foodplanner.model.MealModel
import com.example.foodplanner.network.RetrofitService
import com.example.foodplanner.utils.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilterMealsViewModelFactory(private val retrofit: RetrofitService, private val dao: MealDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterMealsViewModel::class.java)) {
            return FilterMealsViewModel(retrofit, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class FilterMealsViewModel(private val retrofit: RetrofitService, private val dao: MealDao) :
    ViewModel() {
    private val _filteredMeals = MutableLiveData<MealModel>()
    val filteredMeals: LiveData<MealModel> get() = _filteredMeals

    private val _warningMessage = MutableLiveData<String>()
    val warningMessage: LiveData<String> get() = _warningMessage

    private var _uid: String = SharedPrefManager.getUserUID() ?: ""

    fun getFilteredMealsByCategory(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = retrofit.filterByCategory(categoryName)
                withContext(Dispatchers.Main) {
                    if (meal.meals.isEmpty()) {
                        _warningMessage.postValue("No meals found")
                        _filteredMeals.postValue(meal)
                    } else {
                        meal.meals.forEach { checkIfFavorite(it, dao, _uid) }
                        _filteredMeals.postValue(meal)
                    }
                }
            } catch (e: Exception) {
                Log.e("FilterMealsViewModel", "filterByCategory: ${e.message}")
            }
        }
    }

    fun getFilteredMealsByArea(areaName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = retrofit.filterByArea(areaName)
                withContext(Dispatchers.Main) {
                    if (meal.meals.isEmpty()) {
                        _warningMessage.postValue("No meals found")
                        _filteredMeals.postValue(meal)
                    } else {
                        meal.meals.forEach { checkIfFavorite(it, dao, _uid) }
                        _filteredMeals.postValue(meal)
                    }
                }
            } catch (e: Exception) {
                Log.e("FilterMealsViewModel", "filterByArea: ${e.message}")
            }
        }
    }

    fun getFilteredMealsByIngredient(ingredientName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = retrofit.filterByIngredient(ingredientName)
                withContext(Dispatchers.Main) {
                    if (meal.meals.isEmpty()) {
                        _warningMessage.postValue("No meals found")
                        _filteredMeals.postValue(meal)
                    } else {
                        meal.meals.forEach { checkIfFavorite(it, dao, _uid) }
                        _filteredMeals.postValue(meal)
                    }
                }
            } catch (e: Exception) {
                Log.e("FilterMealsViewModel", "filterByIngredient: ${e.message}")
            }
        }
    }

    fun searchMeal(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = retrofit.searchMeal(query)
                withContext(Dispatchers.Main) {
                    if (meal.meals == null || meal.meals.isEmpty()) {
                        _filteredMeals.value = meal
                        _warningMessage.postValue("No meals found")
                    } else {
                        meal.meals.forEach { checkIfFavorite(it, dao, _uid) }
                        _filteredMeals.postValue(meal)
                    }
                }
            } catch (e: Exception) {
                Log.e("FilterMealsViewModel", "filterByQuery: ${e.message}")
            }
        }
    }
}