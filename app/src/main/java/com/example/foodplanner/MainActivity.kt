package com.example.foodplanner

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_nav_graph -> {
                    showFragment(R.id.homeNavHost)
                    true
                }
                R.id.favorites_nav_graph -> {
                    showFragment(R.id.favoritesNavHost)
                    true
                }
                R.id.plan_meals_nav_graph -> {
                    showFragment(R.id.planMealsNavHost)
                    true
                }
                else -> false
            }
        }
    }

    private fun showFragment(fragmentId: Int) {
        val homeNavHost = findViewById<FragmentContainerView>(R.id.homeNavHost)
        val favoritesNavHost = findViewById<FragmentContainerView>(R.id.favoritesNavHost)
        val profileNavHost = findViewById<FragmentContainerView>(R.id.planMealsNavHost)

        homeNavHost.visibility = if (fragmentId == R.id.homeNavHost) View.VISIBLE else View.GONE
        favoritesNavHost.visibility = if (fragmentId == R.id.favoritesNavHost) View.VISIBLE else View.GONE
        profileNavHost.visibility = if (fragmentId == R.id.planMealsNavHost) View.VISIBLE else View.GONE
    }
}
