package com.example.foodplanner.features.home.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.foodplanner.R
import com.example.foodplanner.db.MealDataBase
import com.example.foodplanner.features.home.view.adapters.IngredientsAdapter
import com.example.foodplanner.network.ApiClient
import com.example.foodplanner.utils.IngredientsListener
import com.example.foodplanner.features.filterMeals.view.FilteredMeals
import com.example.foodplanner.features.home.viewModel.MealFactory
import com.example.foodplanner.features.home.viewModel.MealViewModel
import com.example.foodplanner.features.mainLayout.viewModels.NetworkViewModel


class IngredientsFragment : Fragment(), IngredientsListener {
    private lateinit var noInternetImage: ImageView
    private lateinit var noInternetText: TextView
    private lateinit var ingredientsList: RecyclerView
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var mealViewModel: MealViewModel
    private lateinit var networkViewModel: NetworkViewModel
    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingredients, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        setupSearchView()
        setupViewModel()
        networkViewModel.checkInternetConnection()
        networkViewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                noInternetImage.visibility = View.GONE
                noInternetText.visibility = View.GONE
                searchView.visibility = View.VISIBLE
                ingredientsList.visibility = View.VISIBLE
                mealViewModel.getIngredients()
            } else {
                noInternetImage.visibility = View.VISIBLE
                noInternetText.visibility = View.VISIBLE
                ingredientsList.visibility = View.GONE
                loadingAnimation.visibility = View.GONE
                searchView.visibility = View.GONE
            }
        }
        mealViewModel.ingredients.observe(viewLifecycleOwner) {
            loadingAnimation.visibility = View.GONE
            ingredientsAdapter.data = it.meals
            ingredientsAdapter.notifyDataSetChanged()
        }
    }

    private fun initUi(view: View) {
        noInternetImage = view.findViewById(R.id.noInternetImage)
        noInternetText = view.findViewById(R.id.noInternetText)
        ingredientsList = view.findViewById(R.id.ingredientsBody)
        ingredientsAdapter = IngredientsAdapter(requireActivity(), listOf(), this)
        loadingAnimation = view.findViewById(R.id.loadingAnimationView)
        ingredientsList.adapter = ingredientsAdapter
        ingredientsList.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        searchView = view.findViewById(R.id.searchView)
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        if (query.isNullOrBlank()) {
            ingredientsAdapter.data = mealViewModel.ingredients.value?.meals ?: listOf()
        } else {
            val filteredList = mealViewModel.ingredients.value?.meals?.filter {
                it.strIngredient.contains(query, ignoreCase = true)
            } ?: listOf()
            ingredientsAdapter.data = filteredList
        }
        ingredientsAdapter.notifyDataSetChanged()
    }


    private fun setupViewModel() {
        val retrofit = ApiClient.retrofitService
        val mealDao = MealDataBase.getInstance(requireActivity()).mealDao()
        val mealFactory = MealFactory(retrofit, mealDao)
        mealViewModel = ViewModelProvider(this, mealFactory)[MealViewModel::class.java]
        networkViewModel = ViewModelProvider(this)[NetworkViewModel::class.java]
    }

    override fun onIngredientClicked(ingredient: String) {
        val intent = Intent(requireActivity(), FilteredMeals::class.java)
        intent.putExtra("filterBy", arrayOf("i", ingredient))
        startActivity(intent)
    }
}