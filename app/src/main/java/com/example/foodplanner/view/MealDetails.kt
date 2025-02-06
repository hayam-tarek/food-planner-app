package com.example.foodplanner.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.foodplanner.R
import com.example.foodplanner.db.MealDataBase
import com.example.foodplanner.model.Meal
import com.example.foodplanner.model.getIngredientsList
import com.example.foodplanner.model.getMeasuresList
import com.example.foodplanner.network.ApiClient
import com.example.foodplanner.viewModel.MealFactory
import com.example.foodplanner.viewModel.MealViewModel
import com.example.foodplanner.viewModel.NetworkViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MealDetails : AppCompatActivity() {
    private lateinit var mealViewModel: MealViewModel
    private lateinit var networkViewModel: NetworkViewModel
    private lateinit var toolbar: Toolbar
    private lateinit var mealName: TextView
    private lateinit var mealImage: ImageView
    private lateinit var mealArea: TextView
    private lateinit var mealCategory: TextView
    private lateinit var mealInstructions: TextView
    private lateinit var mealVideo: WebView
    private lateinit var fabButton: FloatingActionButton
    private lateinit var ingredientsList: RecyclerView
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var recipeVideo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_meal_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val mealId = intent.getStringExtra("mealId")
        initUI()
        setupViewModel()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        networkViewModel.checkInternetConnection()
        networkViewModel.isConnected.observe(this) { isConnected ->
            if (isConnected) {
                mealViewModel.getMealDetails(mealId!!)
            } else {
                mealViewModel.getFavoriteMealById(mealId!!)
            }
        }

        mealViewModel.mealDetails.observe(this) { meal ->
            meal.meals[0].let {
                updateUi(it)
                mealViewModel.checkIfFavorite(it)
                fabButton.setOnClickListener {
                    mealViewModel.toggleFavorite(meal.meals[0])
                }
            }
        }
        mealViewModel.isFavorite.observe(this) { isFavorite ->
            updateFavoriteIcon(isFavorite)
        }
        mealViewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        mealViewModel.favoriteMeal.observe(this) {
            val meal = it
            updateUi(it)
            mealViewModel.checkIfFavorite(it)
            fabButton.setOnClickListener {
                mealViewModel.toggleFavorite(meal)
            }
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            fabButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.in_fav
                )
            )
        } else {
            fabButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.out_fav
                )
            )
        }
    }

    private fun updateUi(meal: Meal) {
        mealName.text = meal.strMeal
        mealArea.text = meal.strArea
        mealCategory.text = meal.strCategory
        mealInstructions.text = meal.strInstructions
        toolbar.title = meal.strMeal
        ingredientsAdapter.data = meal.getIngredientsList() as List<String>
        ingredientsAdapter.subData = meal.getMeasuresList() as List<String>
        ingredientsAdapter.notifyDataSetChanged()
        loadYouTubeVideo(meal.strYoutube!!)
        runOnUiThread {
            Glide.with(this)
                .load(meal.strMealThumb)
                .transform(RoundedCorners(25))
                .into(mealImage)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun initUI() {
        toolbar = findViewById(R.id.toolbarMealDetails)
        mealName = findViewById(R.id.mealDetailName)
        mealImage = findViewById(R.id.mealDetailImage)
        mealArea = findViewById(R.id.mealDetailArea)
        mealCategory = findViewById(R.id.mealDetailCategory)
        mealInstructions = findViewById(R.id.mealDetailSteps)
        mealVideo = findViewById(R.id.mealDetailVideo)
        recipeVideo = findViewById(R.id.recipeVideo)
        fabButton = findViewById(R.id.fabButton)
        fabButton.imageTintList = null
        ingredientsList = findViewById(R.id.ingredientsList)
        ingredientsAdapter = IngredientsAdapter(this, listOf(), listOf())
        ingredientsList.adapter = ingredientsAdapter
        ingredientsList.layoutManager =
            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun setupViewModel() {
        val retrofit = ApiClient.retrofitService
        val mealDao = MealDataBase.getInstance(this).mealDao()
        val mealFactory = MealFactory(retrofit, mealDao)
        mealViewModel = ViewModelProvider(this, mealFactory)[MealViewModel::class.java]
        networkViewModel = ViewModelProvider(this)[NetworkViewModel::class.java]
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadYouTubeVideo(youtubeUrl: String) {
        try {
            val videoId = youtubeUrl.split("v=")[1]
            val embedUrl = "https://www.youtube.com/embed/$videoId"

            mealVideo.settings.javaScriptEnabled = true
            mealVideo.settings.pluginState = WebSettings.PluginState.ON
            mealVideo.settings.mediaPlaybackRequiresUserGesture = false
            mealVideo.webViewClient = WebViewClient()
            mealVideo.loadUrl(embedUrl)
        } catch (e: Exception) {
            Toast.makeText(this, "No video found", Toast.LENGTH_SHORT).show()
            mealVideo.visibility = WebView.GONE
            recipeVideo.visibility = TextView.GONE
        }
    }
}