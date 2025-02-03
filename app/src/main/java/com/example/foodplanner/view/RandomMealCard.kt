package com.example.foodplanner.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.foodplanner.R
import com.example.foodplanner.network.ApiClient
import com.example.foodplanner.viewModel.MealFactory
import com.example.foodplanner.viewModel.MealViewModel
import com.example.foodplanner.viewModel.NetworkViewModel

class RandomMealCard : Fragment() {
    private lateinit var mealViewModel: MealViewModel
    private val networkViewModel: NetworkViewModel by viewModels()
    private lateinit var mealName: TextView
    private lateinit var mealCountry: TextView
    private lateinit var mealCategory: TextView
    private lateinit var mealImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_random_meal_card, container, false)
        initUI(view)
        setupViewModel()
        networkViewModel.checkInternetConnection()
        networkViewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                mealViewModel.getRandomMeal()
            }
        }
        mealViewModel.randomMeal.observe(viewLifecycleOwner) { meals ->
            meals.meals[0].let {
                mealName.text = it.strMeal
                mealCountry.text = it.strArea
                mealCategory.text = it.strCategory
                requireActivity().runOnUiThread {
                    Glide.with(requireContext())
                        .load(it.strMealThumb)
                        .transform(RoundedCorners(25))
                        .into(mealImage)
                }
                view.setOnClickListener {
                    val intent = Intent(requireContext(), MealDetails::class.java)
                    intent.putExtra("mealId", meals.meals[0].idMeal)
                    startActivity(intent)
                }
            }
        }

        return view
    }

    private fun setupViewModel() {
        val retrofit = ApiClient.retrofitService
        val mealFactory = MealFactory(retrofit)
        mealViewModel = ViewModelProvider(this, mealFactory)[MealViewModel::class.java]
    }

    private fun initUI(view: View) {
        mealName = view.findViewById<TextView>(R.id.mealName)
        mealCountry = view.findViewById<TextView>(R.id.mealCountry)
        mealImage = view.findViewById<ImageView>(R.id.mealImage)
        mealCategory = view.findViewById<TextView>(R.id.mealCategory)
    }
}