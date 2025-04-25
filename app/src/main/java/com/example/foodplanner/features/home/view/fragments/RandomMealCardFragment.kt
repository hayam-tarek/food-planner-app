package com.example.foodplanner.features.home.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.foodplanner.R
import com.example.foodplanner.db.MealDataBase
import com.example.foodplanner.model.Meal
import com.example.foodplanner.network.ApiClient
import com.example.foodplanner.features.home.view.MealDetails
import com.example.foodplanner.features.home.viewModel.MealFactory
import com.example.foodplanner.features.home.viewModel.MealViewModel

class RandomMealCardFragment : Fragment() {

    private lateinit var mealViewModel: MealViewModel
    //    private lateinit var networkViewModel: NetworkViewModel
    private lateinit var mealName: TextView
    private lateinit var mealCountry: TextView
    private lateinit var mealCategory: TextView
    private lateinit var mealImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_random_meal_card, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        setupViewModel()
//        networkViewModel.checkInternetConnection()
//        networkViewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
//            if (isConnected) {
//                mealViewModel.getRandomMeal()
//            }
//        }
        mealViewModel.randomMeal.observe(viewLifecycleOwner) { meals ->
            meals.meals[0].let {
                updateUi(it)
                view.setOnClickListener {
                    val intent = Intent(requireContext(), MealDetails::class.java)
                    intent.putExtra("mealId", meals.meals[0].idMeal)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setupViewModel() {
        val retrofit = ApiClient.retrofitService
        val mealDao = MealDataBase.getInstance(requireContext()).mealDao()
        val mealFactory = MealFactory(retrofit, mealDao)
        mealViewModel = ViewModelProvider(requireActivity(), mealFactory)[MealViewModel::class.java]
//        networkViewModel = ViewModelProvider(requireActivity())[NetworkViewModel::class.java]
    }

    private fun initUI(view: View) {
        mealName = view.findViewById(R.id.mealName)
        mealCountry = view.findViewById(R.id.mealCountry)
        mealImage = view.findViewById(R.id.mealImage)
        mealCategory = view.findViewById(R.id.mealCategory)
    }

    private fun updateUi(meal: Meal) {
        mealName.text = meal.strMeal
        mealCountry.text = meal.strArea
        mealCategory.text = meal.strCategory
        requireActivity().runOnUiThread {
            Glide.with(requireContext())
                .load(meal.strMealThumb)
                .transform(RoundedCorners(25))
                .placeholder(R.drawable.logo_no_background)
                .into(mealImage)
        }
    }
}