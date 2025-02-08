package com.example.foodplanner.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.R
import com.example.foodplanner.db.WeeklyMealDataBase
import com.example.foodplanner.model.WeeklyMeal
import com.example.foodplanner.utils.WeeklyMealListener
import com.example.foodplanner.viewModel.WeeklyMealFactory
import com.example.foodplanner.viewModel.WeeklyMealViewModel


class PlanMealsFragment : Fragment(), WeeklyMealListener {
    private lateinit var weeklyMealsList: RecyclerView
    private lateinit var weeklyMealsAdapter: WeeklyMealsAdapter
    private lateinit var weeklyMealViewModel: WeeklyMealViewModel
    private lateinit var noPlanImage: ImageView
    private lateinit var noPlanTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan_meals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        setUpViewModel()
        weeklyMealViewModel.getWeeklyMeals()
        weeklyMealViewModel.weeklyMeals.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                noPlanImage.visibility = View.GONE
                noPlanTxt.visibility = View.GONE
                weeklyMealsList.visibility = View.VISIBLE
                weeklyMealsAdapter.data = it
                weeklyMealsAdapter.notifyDataSetChanged()
            } else {
                noPlanImage.visibility = View.VISIBLE
                noPlanTxt.visibility = View.VISIBLE
                weeklyMealsList.visibility = View.GONE
            }
        }
        weeklyMealViewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initUi(view: View) {
        weeklyMealsList = view.findViewById(R.id.weeklyMealsList)
        weeklyMealsAdapter = WeeklyMealsAdapter(requireActivity(), listOf(), this)
        noPlanImage = view.findViewById(R.id.noPlanImage)
        noPlanTxt = view.findViewById(R.id.noPlanTxt)
        weeklyMealsList.adapter = weeklyMealsAdapter
        weeklyMealsList.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    private fun setUpViewModel() {
        val dao = WeeklyMealDataBase.getInstance(requireActivity()).weeklyMealDao()
        val factory = WeeklyMealFactory(dao)
        weeklyMealViewModel =
            ViewModelProvider(requireActivity(), factory)[WeeklyMealViewModel::class.java]
    }

    override fun onWeeklyMealClicked(mealId: String) {
        val intent = Intent(requireActivity(), MealDetails::class.java)
        intent.putExtra("mealId", mealId)
        startActivity(intent)
    }

    override fun onDeleteIconClicked(weeklyMeal: WeeklyMeal) {
        weeklyMealViewModel.deleteMeal(weeklyMeal)
    }
}