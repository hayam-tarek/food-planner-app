package com.example.foodplanner

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.foodplanner.viewModel.NetworkViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var networkViewModel: NetworkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        networkViewModel = ViewModelProvider(this)[NetworkViewModel::class.java]

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
//        val color = resources.getColor(R.color.tertiary_color, theme)
        val typedValue = TypedValue()
        theme.resolveAttribute(com.google.android.material.R.attr.colorTertiary, typedValue, true)
        val color = typedValue.data
        bottomNavigationView.itemActiveIndicatorColor = ColorStateList.valueOf(color)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                networkViewModel.checkInternetConnection()
//                Toast.makeText(this, "Refreshing...🔃", Toast.LENGTH_SHORT).show()
                Toasty.info(this, "Updating...", Toast.LENGTH_SHORT, true).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
