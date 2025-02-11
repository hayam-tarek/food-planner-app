package com.example.foodplanner.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.FrameLayout
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
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.foodplanner.R
import com.example.foodplanner.db.MealDataBase
import com.example.foodplanner.db.WeeklyMealDataBase
import com.example.foodplanner.model.Meal
import com.example.foodplanner.model.convertJsonToMeal
import com.example.foodplanner.model.getIngredientsList
import com.example.foodplanner.model.getMeasuresList
import com.example.foodplanner.network.ApiClient
import com.example.foodplanner.viewModel.MealFactory
import com.example.foodplanner.viewModel.MealViewModel
import com.example.foodplanner.viewModel.NetworkViewModel
import com.example.foodplanner.viewModel.WeeklyMealFactory
import com.example.foodplanner.viewModel.WeeklyMealViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.dmoral.toasty.Toasty

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
    private lateinit var favoriteBtn: FloatingActionButton
    private lateinit var mealDetailsBody: FrameLayout
    private lateinit var loadingAnimationView: LottieAnimationView
    private lateinit var nothingFoundAnimationView: LottieAnimationView
    private lateinit var ingredientsList: RecyclerView
    private lateinit var ingredientsAdapter: MealIngredientsAdapter
    private lateinit var recipeVideo: TextView
    private lateinit var addToPlanBtn: Button
    private lateinit var weeklyMealViewModel: WeeklyMealViewModel
    private val daysOfWeek =
        listOf("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

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
                weeklyMealViewModel.getMealById(mealId)
            }
        }

        mealViewModel.mealDetails.observe(this) {
            if (it.meals.isNotEmpty()) {
                setupMealObservers(it.meals[0])
            } else {
                loadingAnimationView.visibility = View.GONE
                nothingFoundAnimationView.visibility = View.VISIBLE
            }
        }
        mealViewModel.isFavorite.observe(this) { isFavorite ->
            updateFavoriteIcon(isFavorite)
        }
//        mealViewModel.message.observe(this) {
//            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//        }
        mealViewModel.infoMessage.observe(this) {
            Toasty.info(this, it, Toast.LENGTH_SHORT).show()
        }
        mealViewModel.warningMessage.observe(this) {
            Toasty.warning(this, it, Toast.LENGTH_SHORT).show()
        }
        mealViewModel.successMessage.observe(this) {
            Toasty.success(this, it, Toast.LENGTH_SHORT).show()
        }
        mealViewModel.errorMessage.observe(this) {
            Toasty.error(this, it, Toast.LENGTH_SHORT).show()
        }
        mealViewModel.favoriteMeal.observe(this) {
            setupMealObservers(it)
        }
//        weeklyMealViewModel.message.observe(this) {
//            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//        }
        weeklyMealViewModel.infoMessage.observe(this) {
            Toasty.info(this, it, Toast.LENGTH_SHORT).show()
        }
        weeklyMealViewModel.warningMessage.observe(this) {
            Toasty.warning(this, it, Toast.LENGTH_SHORT).show()
        }
        weeklyMealViewModel.successMessage.observe(this) {
            Toasty.success(this, it, Toast.LENGTH_SHORT).show()
        }
        weeklyMealViewModel.errorMessage.observe(this) {
            Toasty.error(this, it, Toast.LENGTH_SHORT).show()
        }
        weeklyMealViewModel.mealById.observe(this) {
            if (it != null) {
                val meal = convertJsonToMeal(it.mealJson)
                setupMealObservers(meal)
            }
        }

    }

    private fun setupMealObservers(meal: Meal) {
        favoriteBtn.visibility = View.VISIBLE
        mealDetailsBody.visibility = View.VISIBLE
        addToPlanBtn.visibility = View.VISIBLE
        loadingAnimationView.visibility = View.GONE
        updateUi(meal)
        mealViewModel.checkIfFavorite(meal)

        favoriteBtn.setOnClickListener {
            mealViewModel.toggleFavorite(meal)
        }

        addToPlanBtn.setOnClickListener {
            showDayPickerDialog(meal)
        }
    }

    private fun showDayPickerDialog(meal: Meal) {
        var selectedDayIndex = -1
        var selectedDay = ""
        MaterialDialog(this, BottomSheet()).show {
            cornerRadius(25f)
            title(text = "Select a Day 📌")
            var result = listItemsSingleChoice(
                items = daysOfWeek,
                initialSelection = -1
            ) { _, index, _ ->
                selectedDayIndex = index
                if (selectedDayIndex != -1) {
                    selectedDay = daysOfWeek[selectedDayIndex]
                    weeklyMealViewModel.insertMeal(meal, selectedDay)
                } else {
                    Toasty.info(this@MealDetails, "Please select a day", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            positiveButton(text = "✅ Confirm") {

            }

            negativeButton(text = "❌ Cancel") {
                dismiss()
            }
        }

    }


    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val icon = if (isFavorite) R.drawable.in_fav else R.drawable.out_fav
        favoriteBtn.setImageDrawable(ContextCompat.getDrawable(this, icon))
        if (isFavorite) {
            favoriteBtn.drawable.setTint(
                ContextCompat.getColor(
                    this,
                    android.R.color.holo_red_dark
                )
            )
        } else {
            favoriteBtn.drawable.setTint(ContextCompat.getColor(this, android.R.color.darker_gray))
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
        if (meal.strYoutube != null) loadYouTubeVideo(meal.strYoutube)
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
        favoriteBtn = findViewById(R.id.favoriteBtn)
        addToPlanBtn = findViewById(R.id.addToPlanBtn)
        loadingAnimationView = findViewById(R.id.loadingAnimationView)
        nothingFoundAnimationView = findViewById(R.id.nothingFoundAnimationView)
        mealDetailsBody = findViewById(R.id.mealDetailsBody)
        favoriteBtn.imageTintList = null
        ingredientsList = findViewById(R.id.ingredientsList)
        ingredientsAdapter = MealIngredientsAdapter(this, listOf(), listOf())
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
        val weeklyMealDao = WeeklyMealDataBase.getInstance(this).weeklyMealDao()
        val weeklyMealFactory = WeeklyMealFactory(weeklyMealDao)
        weeklyMealViewModel =
            ViewModelProvider(this, weeklyMealFactory)[WeeklyMealViewModel::class.java]
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
            Toasty.warning(this, "No video found", Toast.LENGTH_SHORT).show()
            mealVideo.visibility = WebView.GONE
            recipeVideo.visibility = TextView.GONE
        }
    }
}