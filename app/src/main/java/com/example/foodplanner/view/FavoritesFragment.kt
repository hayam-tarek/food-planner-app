package com.example.foodplanner.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
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

class FavoritesFragment : Fragment(), MealListener {
    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var mealViewModel: MealViewModel
    private lateinit var mealsAdapter: MealsAdapter
    private lateinit var noFavsImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        setupViewModel()
        mealViewModel.getFavorites()
        mealViewModel.favorites.observe(viewLifecycleOwner) { meals ->
            if (meals.isNotEmpty()) {
                favoritesRecyclerView.visibility = View.VISIBLE
                noFavsImage.visibility = View.GONE
                mealsAdapter.data = meals
                mealsAdapter.notifyDataSetChanged()
            } else {
                favoritesRecyclerView.visibility = View.GONE
                noFavsImage.visibility = View.VISIBLE
            }

        }
    }

    override fun onResume() {
        super.onResume()
        mealViewModel.getFavorites()
    }

    private fun initUI(view: View) {
        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView)
        noFavsImage = view.findViewById(R.id.noFavsImage)
        mealsAdapter = MealsAdapter(requireContext(), listOf(), this)
        favoritesRecyclerView.adapter = mealsAdapter
        favoritesRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun setupViewModel() {
        val retrofit = ApiClient.retrofitService
        val mealDao = MealDataBase.getInstance(requireContext()).mealDao()
        val mealFactory = MealFactory(retrofit, mealDao)
        mealViewModel = ViewModelProvider(requireActivity(), mealFactory)[MealViewModel::class.java]

    }

    override fun onMealClicked(meal: Meal) {
        val intent = Intent(requireContext(), MealDetails::class.java)
        intent.putExtra("mealId", meal.idMeal)
        startActivity(intent)
    }

    override fun onMealFavClicked(meal: Meal) {
        TODO("Not yet implemented")
    }
}