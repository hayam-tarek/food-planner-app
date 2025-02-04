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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.foodplanner.R
import com.example.foodplanner.db.MealDataBase
import com.example.foodplanner.model.getIngredientsList
import com.example.foodplanner.model.getMeasuresList
import com.example.foodplanner.network.ApiClient
import com.example.foodplanner.viewModel.MealFactory
import com.example.foodplanner.viewModel.MealViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MealDetails : AppCompatActivity() {
    private lateinit var mealViewModel: MealViewModel
    private lateinit var toolbar: Toolbar
    private lateinit var mealName: TextView
    private lateinit var mealImage: ImageView
    private lateinit var mealArea: TextView
    private lateinit var mealCategory: TextView
    private lateinit var mealInstructions: TextView
    private lateinit var mealVideo: WebView
    private lateinit var fabButton: FloatingActionButton
    private lateinit var ingredientsList: RecyclerView
    private lateinit var itemsAdapter: ItemsAdapter

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
        mealViewModel.getMealDetails(mealId!!)
        mealViewModel.mealDetails.observe(this) { meal ->
            meal.meals[0].let {
                mealName.text = it.strMeal
                mealArea.text = it.strArea
                mealCategory.text = it.strCategory
                mealInstructions.text = it.strInstructions
                toolbar.title = it.strMeal
                itemsAdapter.data = it.getIngredientsList() as List<String>
                itemsAdapter.subData = it.getMeasuresList() as List<String>
                itemsAdapter.notifyDataSetChanged()
                loadYouTubeVideo(it.strYoutube!!)
                runOnUiThread {
                    Glide.with(this)
                        .load(it.strMealThumb)
                        .transform(RoundedCorners(25))
                        .into(mealImage)
                }
                fabButton.setOnClickListener {
                    mealViewModel.addMealToFav(meal.meals[0])
                }
            }
        }
        mealViewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
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
        fabButton = findViewById(R.id.fabButton)
        ingredientsList = findViewById(R.id.ingredientsList)
        itemsAdapter = ItemsAdapter(this, listOf(), listOf())
        ingredientsList.adapter = itemsAdapter
        ingredientsList.layoutManager =
            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
//        ingredientsList.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupViewModel() {
        val retrofit = ApiClient.retrofitService
        val mealDao = MealDataBase.getInstance(this).mealDao()
        val mealFactory = MealFactory(retrofit, mealDao)
        mealViewModel = ViewModelProvider(this, mealFactory)[MealViewModel::class.java]
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadYouTubeVideo(youtubeUrl: String) {
        val videoId = youtubeUrl.split("v=")[1] // Extract YouTube video ID
        val embedUrl = "https://www.youtube.com/embed/$videoId"

        mealVideo.settings.javaScriptEnabled = true
        mealVideo.settings.pluginState = WebSettings.PluginState.ON
        mealVideo.settings.mediaPlaybackRequiresUserGesture = false
        mealVideo.webViewClient = WebViewClient()
        mealVideo.loadUrl(embedUrl)
    }
}