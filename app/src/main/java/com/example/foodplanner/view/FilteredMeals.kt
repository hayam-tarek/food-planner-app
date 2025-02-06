package com.example.foodplanner.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
        mealViewModel.filteredMeals.observe(this) { meal ->
            mealsAdapter.data = meal.meals
            mealsAdapter.notifyDataSetChanged()
        }
        mealViewModel.isFavorite.observe(this) { isFav ->
            mealsAdapter.notifyDataSetChanged()
        }
    }

    private fun initUI() {
        toolbar = findViewById(R.id.toolbar2)
        mealsList = findViewById(R.id.mealsList)
        mealsAdapter = MealsAdapter(this, listOf(), this)
        mealsList.adapter = mealsAdapter
        mealsList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

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