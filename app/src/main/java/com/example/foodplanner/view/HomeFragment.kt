package com.example.foodplanner.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.foodplanner.R
import com.example.foodplanner.db.MealDataBase
import com.example.foodplanner.network.ApiClient
import com.example.foodplanner.utils.AreaListener
import com.example.foodplanner.utils.CategoryListener
import com.example.foodplanner.utils.SharedPrefManager
import com.example.foodplanner.viewModel.MealFactory
import com.example.foodplanner.viewModel.MealViewModel
import com.example.foodplanner.viewModel.NetworkViewModel

class HomeFragment : Fragment(), CategoryListener, AreaListener {

    private lateinit var noInternetImage: ImageView
    private lateinit var mainContent: ScrollView
    private lateinit var networkViewModel: NetworkViewModel
    private lateinit var countriesList: RecyclerView
    private lateinit var areasAdapter: AreasAdapter
    private lateinit var mealViewModel: MealViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var categoriesList: RecyclerView
    private lateinit var tapToSearch: ImageView
    private lateinit var searchBar: EditText
    private lateinit var noInternetTxt: TextView
    private lateinit var seeAllAreas: TextView
    private lateinit var countriesTxt: TextView
    private lateinit var categoriesTxt: TextView
    private var areasIsGridLayout = false

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
        networkViewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                noInternetImage.visibility = View.GONE
                noInternetTxt.visibility = View.GONE
                mainContent.visibility = View.VISIBLE
                if (SharedPrefManager.getUserUID() != null) {
                    mealViewModel.getAreas()
                    countriesList.visibility = View.VISIBLE
                }
                mealViewModel.getCategories()
                mealViewModel.getRandomMeal()
            } else {
                noInternetImage.visibility = View.VISIBLE
                mainContent.visibility = View.GONE
                noInternetTxt.visibility = View.VISIBLE
            }
        }
        mealViewModel.areas.observe(viewLifecycleOwner) { areas ->
            areasAdapter.data = areas.meals
            areasAdapter.notifyDataSetChanged()
            countriesTxt.visibility = View.VISIBLE
            seeAllAreas.visibility = View.VISIBLE
        }
        mealViewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.data = categories.categories
            categoriesAdapter.notifyDataSetChanged()
            categoriesTxt.visibility = View.VISIBLE
        }
        tapToSearch.setOnClickListener {
            val query = searchBar.text.toString().trim()
            if (query.isNotEmpty()) {
                val intent = Intent(requireActivity(), FilteredMeals::class.java)
                intent.putExtra("filterBy", arrayOf("s", query))
                startActivity(intent)
            } else {
                searchBar.error = "Please enter a meal name"
            }
        }
        seeAllAreas.setOnClickListener {
            toggleAreasViewLayout()
        }
    }

    private fun toggleAreasViewLayout() {
        if (areasIsGridLayout) {
            countriesList.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            seeAllAreas.text = "See All"
        } else {
            countriesList.layoutManager = StaggeredGridLayoutManager(
                4,
                StaggeredGridLayoutManager.VERTICAL
            )
            seeAllAreas.text = "See Less"
        }
        areasIsGridLayout = !areasIsGridLayout
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
        areasAdapter = AreasAdapter(requireActivity(), listOf(), this)
        searchBar = view.findViewById(R.id.searchBar)
        tapToSearch = view.findViewById(R.id.tapToSearch)
        noInternetTxt = view.findViewById(R.id.noInternetText)
        countriesList.adapter = areasAdapter
        countriesList.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        categoriesList = view.findViewById(R.id.categoriesList)
        categoriesAdapter = CategoriesAdapter(requireActivity(), listOf(), this)
        categoriesList.adapter = categoriesAdapter
        categoriesList.layoutManager = GridLayoutManager(requireActivity(), 3)
        seeAllAreas = view.findViewById(R.id.seeAllAreas)
        countriesTxt = view.findViewById(R.id.countriesTxt)
        categoriesTxt = view.findViewById(R.id.categoriesTxt)
    }

    override fun onCategoryClicked(category: String) {
        val intent = Intent(requireActivity(), FilteredMeals::class.java)
        intent.putExtra("filterBy", arrayOf("c", category))
        startActivity(intent)
    }

    override fun onAreaClicked(area: String) {
        val intent = Intent(requireActivity(), FilteredMeals::class.java)
        intent.putExtra("filterBy", arrayOf("a", area))
        startActivity(intent)
    }
}