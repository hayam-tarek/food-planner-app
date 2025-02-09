package com.example.foodplanner.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.foodplanner.R
import com.example.foodplanner.db.MealDataBase
import com.example.foodplanner.model.Meal
import com.example.foodplanner.network.ApiClient
import com.example.foodplanner.utils.MealListener
import com.example.foodplanner.viewModel.MealFactory
import com.example.foodplanner.viewModel.MealViewModel

class FilteredMeals : AppCompatActivity(), MealListener {
    private lateinit var toolbar: Toolbar
    private lateinit var mealViewModel: MealViewModel
    private lateinit var mealsAdapter: MealsAdapter
    private lateinit var mealsList: RecyclerView
    private lateinit var nothingImage: ImageView
    private lateinit var searchAnimation: LottieAnimationView
    private lateinit var loadingAnimation: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_filtered_meals)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val filterBy = intent.getStringArrayExtra("filterBy")?.get(0)
        val type = intent.getStringArrayExtra("filterBy")?.get(1)

        initUI()
        toolbar.title = type
        setupViewModel()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (filterBy == "c") {
            mealViewModel.getFilteredMealsByCategory(type!!)
        }
        if (filterBy == "a") {
            mealViewModel.getFilteredMealsByArea(type!!)
        }
        if (filterBy == "i") {
            mealViewModel.getFilteredMealsByIngredient(type!!)
        }
        if (filterBy == "s") {
            mealViewModel.searchMeal(type!!)
        }
        mealViewModel.filteredMeals.observe(this) { meal ->
            if (meal.meals.isNullOrEmpty()) {
                mealsList.visibility = View.GONE
                loadingAnimation.visibility = View.GONE
                searchAnimation.visibility = View.VISIBLE
            } else {
                mealsAdapter.data = meal.meals
                mealsAdapter.notifyDataSetChanged()
                mealsList.visibility = View.VISIBLE
                searchAnimation.visibility = View.GONE
                loadingAnimation.visibility = View.GONE
            }
        }
        mealViewModel.isFavorite.observe(this) { isFav ->
            mealsAdapter.notifyDataSetChanged()
        }
        mealViewModel.message.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initUI() {
        toolbar = findViewById(R.id.toolbar2)
        mealsList = findViewById(R.id.mealsList)
        nothingImage = findViewById(R.id.nothingImage)
        mealsAdapter = MealsAdapter(this, listOf(), this)
        mealsList.adapter = mealsAdapter
        mealsList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        searchAnimation = findViewById(R.id.searchAnimationView)
        loadingAnimation = findViewById(R.id.loadingAnimationView)

    }

    private fun setupViewModel() {
        val retrofit = ApiClient.retrofitService
        val mealDao = MealDataBase.getInstance(this).mealDao()
        val mealFactory = MealFactory(retrofit, mealDao)
        mealViewModel = ViewModelProvider(this, mealFactory)[MealViewModel::class.java]
    }

    override fun onMealClicked(meal: Meal) {
        val intent = Intent(this, MealDetails::class.java)
        intent.putExtra("mealId", meal.idMeal)
        startActivity(intent)
    }

    override fun onMealFavClicked(meal: Meal) {
        mealViewModel.toggleFavorite(meal)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}