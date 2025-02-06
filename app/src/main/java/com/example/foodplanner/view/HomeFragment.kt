package com.example.foodplanner.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.R
import com.example.foodplanner.db.MealDataBase
import com.example.foodplanner.network.ApiClient
import com.example.foodplanner.viewModel.MealFactory
import com.example.foodplanner.viewModel.MealViewModel
import com.example.foodplanner.viewModel.NetworkViewModel

class HomeFragment : Fragment() {

    private lateinit var noInternetImage: ImageView
    private lateinit var mainContent: ScrollView
    private lateinit var networkViewModel: NetworkViewModel
    private lateinit var countriesList: RecyclerView
    private lateinit var areasAdapter: AreasAdapter
    private lateinit var mealViewModel: MealViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var categoriesList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        setupViewModel()
        networkViewModel.checkInternetConnection()
        networkViewModel.isConnected.observe(viewLifecycleOwner, Observer { isConnected ->
            if (isConnected) {
                noInternetImage.visibility = View.GONE
                mainContent.visibility = View.VISIBLE
                mealViewModel.getAreas()
                mealViewModel.getCategories()
            } else {
                noInternetImage.visibility = View.VISIBLE
                mainContent.visibility = View.GONE
            }
        })
        mealViewModel.areas.observe(viewLifecycleOwner, Observer { areas ->
            areasAdapter.data = areas.meals
            areasAdapter.notifyDataSetChanged()
        })
        mealViewModel.categories.observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.data = categories.categories
            categoriesAdapter.notifyDataSetChanged()
        })
    }

    private fun setupViewModel() {
        val retrofit = ApiClient.retrofitService
        val mealDao = MealDataBase.getInstance(requireActivity()).mealDao()
        val mealFactory = MealFactory(retrofit, mealDao)
        mealViewModel = ViewModelProvider(requireActivity(), mealFactory)[MealViewModel::class.java]
        networkViewModel = ViewModelProvider(requireActivity())[NetworkViewModel::class.java]
    }

    private fun initUi(view: View) {
        noInternetImage = view.findViewById(R.id.noInternetImage)
        mainContent = view.findViewById(R.id.mainContent)
        countriesList = view.findViewById(R.id.countriesList)
        areasAdapter = AreasAdapter(requireActivity(), listOf())
        countriesList.adapter = areasAdapter
        countriesList.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        categoriesList = view.findViewById(R.id.categoriesList)
        categoriesAdapter = CategoriesAdapter(requireActivity(), listOf())
        categoriesList.adapter = categoriesAdapter
        categoriesList.layoutManager = GridLayoutManager(requireActivity(), 3)
    }
}