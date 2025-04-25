package com.example.foodplanner.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.core.utils.checkIfFavorite
import com.example.foodplanner.db.MealDao
import com.example.foodplanner.model.AreaModel
import com.example.foodplanner.model.CategoryModel
import com.example.foodplanner.model.IngredientModel
import com.example.foodplanner.model.MealModel
import com.example.foodplanner.network.RetrofitService
import com.example.foodplanner.utils.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModelFactory(private val retrofit: RetrofitService, private val dao: MealDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(retrofit, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class HomeViewModel(private val retrofit: RetrofitService, private val dao: MealDao) : ViewModel() {
    private val _randomMeal = MutableLiveData<MealModel>()
    val randomMeal: LiveData<MealModel> get() = _randomMeal

    private val _mealDetails = MutableLiveData<MealModel>()
    val mealDetails: LiveData<MealModel> get() = _mealDetails

    private val _areas = MutableLiveData<AreaModel>()
    val areas: LiveData<AreaModel> get() = _areas

    private val _categories = MutableLiveData<CategoryModel>()
    val categories: LiveData<CategoryModel> get() = _categories

    private val _ingredients = MutableLiveData<IngredientModel>()
    val ingredients: LiveData<IngredientModel> get() = _ingredients

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _warningMessage = MutableLiveData<String>()
    val warningMessage: LiveData<String> get() = _warningMessage

    private var _uid: String = SharedPrefManager.getUserUID() ?: ""

    fun getRandomMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = retrofit.randomMeal()
                withContext(Dispatchers.Main) {
                    if (meal.meals.isEmpty()) {
                        _warningMessage.postValue("No meal found")
                    } else {
                        _randomMeal.postValue(meal)
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "getRandomMeal: ${e.message}")
            }
        }
    }

    fun getMealDetails(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = retrofit.mealDetails(mealId)
                withContext(Dispatchers.Main) {
                    if (meal.meals.isEmpty()) {
                        _warningMessage.postValue("No meal found")
                        _mealDetails.postValue(meal)
                    } else {
                        val isFavoriteMeal = checkIfFavorite(
                            meal.meals[0],
                            dao,
                            _uid
                        )
                        _isFavorite.value = isFavoriteMeal
                        _mealDetails.postValue(meal)
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "getMealDetails: ${e.message}")
            }
        }
    }

    fun getAreas() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val areas = retrofit.areas()
                withContext(Dispatchers.Main) {
                    if (areas.meals.isEmpty()) {
                        _warningMessage.postValue("No areas found")
                    } else {
                        _areas.postValue(areas)
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "getAreas: ${e.message}")
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categories = retrofit.categories()
                withContext(Dispatchers.Main) {
                    if (categories.categories.isEmpty()) {
                        _warningMessage.postValue("No categories found")
                    } else {
                        _categories.postValue(categories)
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "getCategories: ${e.message}")
            }
        }
    }

    fun getIngredients() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ingredients = retrofit.ingredients()
                withContext(Dispatchers.Main) {
                    if (ingredients.meals.isEmpty()) {
                        _warningMessage.postValue("No ingredients found")
                    } else {
                        _ingredients.postValue(ingredients)
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "getIngredients: ${e.message}")
            }
        }
    }
}