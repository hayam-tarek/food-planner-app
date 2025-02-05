package com.example.foodplanner.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.foodplanner.R
import com.example.foodplanner.db.MealDataBase
import com.example.foodplanner.network.ApiClient
import com.example.foodplanner.viewModel.MealFactory
import com.example.foodplanner.viewModel.MealViewModel

class FavoritesFragment : Fragment() {
    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var mealViewModel: MealViewModel
    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        initUI(view)
        setupViewModel()
        mealViewModel.getFavorites()
        mealViewModel.favorites.observe(viewLifecycleOwner) { meals ->
            mealsAdapter.data = meals
            mealsAdapter.notifyDataSetChanged()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initUI(view: View) {
        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView)
        mealsAdapter = MealsAdapter(requireContext(), listOf())
        favoritesRecyclerView.adapter = mealsAdapter
        favoritesRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun setupViewModel() {
        val retrofit = ApiClient.retrofitService
        val mealDao = MealDataBase.getInstance(requireContext()).mealDao()
        val mealFactory = MealFactory(retrofit, mealDao)
        mealViewModel = ViewModelProvider(this, mealFactory)[MealViewModel::class.java]

    }
}