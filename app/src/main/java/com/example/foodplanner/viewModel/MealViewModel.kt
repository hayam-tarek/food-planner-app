package com.example.foodplanner.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.db.MealDao
import com.example.foodplanner.model.AreaModel
import com.example.foodplanner.model.CategoryModel
import com.example.foodplanner.model.IngredientModel
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

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _areas = MutableLiveData<AreaModel>()
    val areas: LiveData<AreaModel> get() = _areas

    private val _categories = MutableLiveData<CategoryModel>()
    val categories: LiveData<CategoryModel> get() = _categories

    private val _ingredients = MutableLiveData<IngredientModel>()
    val ingredients: LiveData<IngredientModel> get() = _ingredients

    private val _filteredMeals = MutableLiveData<MealModel>()
    val filteredMeals: LiveData<MealModel> get() = _filteredMeals

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> get() = _successMessage

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _warningMessage = MutableLiveData<String>()
    val warningMessage: LiveData<String> get() = _warningMessage

    private val _infoMessage = MutableLiveData<String>()
    val infoMessage: LiveData<String> get() = _infoMessage

    init {
        getFavorites()
    }

    fun getRandomMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = retrofit.randomMeal()
                withContext(Dispatchers.Main) {
                    if (meal.meals.isEmpty()) {
                        _warningMessage.postValue("No meal found")
                    } else {
                        checkIfFavorite(meal.meals[0])
                        _randomMeal.postValue(meal)
                    }
                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error fetching meal: ${e.message}"
//                }
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
                        _warningMessage.postValue("No meal found")
                        _mealDetails.postValue(meal)
                    } else {
                        checkIfFavorite(meal.meals[0])
                        _mealDetails.postValue(meal)
                    }
                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error fetching meal: ${e.message}"
//                }
                Log.i("MealViewModel", "getMealDetails: ${e.message}")
            }
        }
    }

    fun getFilteredMealsByCategory(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meal = retrofit.filterByCategory(categoryName)
                withContext(Dispatchers.Main) {
                    if (meal.meals.isEmpty()) {
                        _warningMessage.postValue("No meals found")
                        _filteredMeals.postValue(meal)
                    } else {
                        meal.meals.forEach { checkIfFavorite(it) }
                        _filteredMeals.postValue(meal)
                    }
                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error fetching meals: ${e.message}"
//                }
                Log.i("MealViewModel", "filterByCategory: ${e.message}")
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
                        meal.meals.forEach { checkIfFavorite(it) }
                        _filteredMeals.postValue(meal)
                    }
                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error fetching meals: ${e.message}"
//                }
                Log.i("MealViewModel", "filterByArea: ${e.message}")
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
                        meal.meals.forEach { checkIfFavorite(it) }
                        _filteredMeals.postValue(meal)
                    }
                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error fetching meals: ${e.message}"
//                }
                Log.i("MealViewModel", "filterByIngredient: ${e.message}")
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
                        meal.meals.forEach { checkIfFavorite(it) }
                        _filteredMeals.postValue(meal)
                    }
                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error fetching meals: ${e.message}"
//                }
                Log.i("MealViewModel", "filterByQuery: ${e.message}")
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
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error fetching areas: ${e.message}"
//                }
                Log.i("MealViewModel", "getAreas: ${e.message}")
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
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error fetching categories: ${e.message}"
//                }
                Log.i("MealViewModel", "getCategories: ${e.message}")
            }
        }
    }

    fun getIngredients(){
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
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error fetching ingredients: ${e.message}"
//                }
                Log.i("MealViewModel", "getIngredients: ${e.message}")
            }
        }
    }

    fun addMealToFav(meal: Meal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = dao.insert(meal)
                withContext(Dispatchers.Main) {
                    if (result == -1L) {
                        _infoMessage.postValue("Meal already exists in the favorites")
                    } else {
                        meal.isFavorite = true
                        _successMessage.postValue("Meal added to the favorites")
                    }
                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error adding meal to favorites: ${e.message}"
//                }
                Log.i("MealViewModel", "addMealToDb: ${e.message}")
            }
        }
    }

    fun deleteMealFromFav(meal: Meal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.delete(meal)
                withContext(Dispatchers.Main) {
                    meal.isFavorite = false
                    _successMessage.postValue("Meal deleted from the favorites")
                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error deleting meal from favorites: ${e.message}"
//                }
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
                        _favorites.postValue(listOf())
//                        _warningMessage.postValue("No favorites found")
                    } else {
                        meals.forEach { it.isFavorite = true }
                        _favorites.postValue(meals)
                    }
                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error fetching favorites: ${e.message}"
//                }
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
                        _infoMessage.postValue("This meal is not in the favorites")
                    } else {
                        meal.isFavorite = true
                        _favoriteMeal.postValue(meal)
                    }
                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error fetching meal: ${e.message}"
//                }
                Log.i("MealViewModel", "getFavoriteMealById: ${e.message}")
            }
        }
    }

    fun toggleFavorite(meal: Meal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var fullMeal: Meal = meal
                if (meal.strArea == null || meal.strCategory == null) {
                    val mealDetails = retrofit.mealDetails(meal.idMeal)
                    if (mealDetails.meals.isNotEmpty()) {
                        fullMeal = mealDetails.meals[0]
                    }
                }
                val existingMeal = dao.getMealById(meal.idMeal.toInt())
                withContext(Dispatchers.Main) {
                    if (existingMeal != null) {
                        dao.delete(meal)
                        meal.isFavorite = false
                        _isFavorite.postValue(false)
                        _successMessage.postValue("Meal removed from favorites")
                    } else {
                        dao.insert(fullMeal)
                        meal.isFavorite = true
                        _isFavorite.postValue(true)
                        _successMessage.postValue("Meal added to favorites")
                    }
                    getFavorites()
                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error toggling favorite: ${e.message}"
//                }
                Log.i("MealViewModel", "toggleFavorite: ${e.message}")
            }
        }
    }

    fun checkIfFavorite(meal: Meal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = dao.getMealById(meal.idMeal.toInt())
                withContext(Dispatchers.Main) {
                    meal.isFavorite = result != null
                    _isFavorite.value = result != null
                }
            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _message.value = "Error checking if meal is favorite: ${e.message}"
//                }
                Log.i("MealViewModel", "checkIfFavorite: ${e.message}")
            }
        }
    }
}