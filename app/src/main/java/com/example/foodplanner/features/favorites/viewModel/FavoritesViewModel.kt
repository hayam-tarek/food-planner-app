//package com.example.foodplanner.view.favorites
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import com.example.foodplanner.db.MealDao
//import com.example.foodplanner.model.Meal
//import com.example.foodplanner.model.MealModel
//import com.example.foodplanner.network.RetrofitService
//import com.example.foodplanner.utils.SharedPrefManager
//import com.example.foodplanner.core.utils.checkIfFavorite
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class FavoritesViewModelFactory(private val retrofit: RetrofitService, private val dao: MealDao) :
//    ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
//            return FavoritesViewModel(retrofit, dao) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
//
//class FavoritesViewModel(private val retrofit: RetrofitService, private val dao: MealDao) : ViewModel() {
//    private val _favorites = MutableLiveData<List<Meal>>()
//    val favorites: LiveData<List<Meal>> get() = _favorites
//
//    private val _favoriteMeal = MutableLiveData<Meal>()
//    val favoriteMeal: LiveData<Meal> get() = _favoriteMeal
//
//    private val _isFavorite = MutableLiveData<Boolean>()
//    val isFavorite: LiveData<Boolean> get() = _isFavorite
//
//    private val _successMessage = MutableLiveData<String>()
//    val successMessage: LiveData<String> get() = _successMessage
//
//    private val _infoMessage = MutableLiveData<String>()
//    val infoMessage: LiveData<String> get() = _infoMessage
//
//    private val _warningMessage = MutableLiveData<String>()
//    val warningMessage: LiveData<String> get() = _warningMessage
//
//    private var _uid: String = SharedPrefManager.getUserUID() ?: ""
//
//    init {
//        getFavorites()
//    }
//
//    fun getFavorites() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val meals = dao.getAll(_uid)
//                withContext(Dispatchers.Main) {
//                    if (meals.isEmpty()) {
//                        _favorites.postValue(listOf())
//                    } else {
//                        meals.forEach { it.isFavorite = true }
//                        _favorites.postValue(meals)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.i("FavoritesViewModel", "getFavorites: ${e.message}")
//            }
//        }
//    }
//
//    fun getFavoriteMealById(mealId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val meal = dao.getMealById(mealId.toInt(), _uid)
//                withContext(Dispatchers.Main) {
//                    if (meal == null) {
//                        _infoMessage.postValue("This meal is not in the favorites")
//                    } else {
//                        meal.isFavorite = true
//                        _favoriteMeal.postValue(meal)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.i("FavoritesViewModel", "getFavoriteMealById: ${e.message}")
//            }
//        }
//    }
//
//    fun toggleFavorite(meal: Meal) {
//        if (_uid.isEmpty()) {
//            _warningMessage.value = "Login to add to favorites"
//            return
//        }
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                var fullMeal: Meal = meal
//                if (meal.strArea == null || meal.strCategory == null) {
//                    val mealDetails = retrofit.mealDetails(meal.idMeal)
//                    if (mealDetails.meals.isNotEmpty()) {
//                        fullMeal = mealDetails.meals[0]
//                    }
//                }
//                val isFavorite = checkIfFavorite(meal, dao, _uid)
//                withContext(Dispatchers.Main) {
//                    if (isFavorite) {
//                        dao.delete(meal.idMeal, _uid)
//                        meal.isFavorite = false
//                        _isFavorite.postValue(false)
//                        _successMessage.postValue("Meal removed from favorites")
//                    } else {
//                        dao.insert(fullMeal.copy(uid = _uid))
//                        meal.isFavorite = true
//                        _isFavorite.postValue(true)
//                        _successMessage.postValue("Meal added to favorites")
//                    }
//                    getFavorites()
//                }
//            } catch (e: Exception) {
//                Log.i("FavoritesViewModel", "toggleFavorite: ${e.message}")
//            }
//        }
//    }
//}