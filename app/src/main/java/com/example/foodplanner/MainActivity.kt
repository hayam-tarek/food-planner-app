package com.example.foodplanner

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.foodplanner.utils.AuthState
import com.example.foodplanner.utils.SharedPrefManager
import com.example.foodplanner.view.LoginActivity
import com.example.foodplanner.view.WelcomeActivity
import com.example.foodplanner.viewModel.AuthViewModel
import com.example.foodplanner.viewModel.NetworkViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var networkViewModel: NetworkViewModel
    private lateinit var authViewModel: AuthViewModel

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
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        authViewModel.authState.observe(this) {
            if (it == AuthState.AUTH_SUCCESS) {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        authViewModel.errorMessage.observe(this) {
            Toasty.error(this, it, Toast.LENGTH_SHORT, true).show()
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
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
                Toasty.info(this, "Updating...", Toast.LENGTH_SHORT, true).show()
                true
            }

            R.id.menu_icon -> {
                val menuView = findViewById<View>(R.id.menu_icon)
                if (menuView != null) {
                    val popupMenu = PopupMenu(this, menuView)
                    popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.action_signout -> {
                                if (networkViewModel.isConnected.value == true) {
                                    authViewModel.signOut()
                                } else {
                                    Toasty.error(
                                        this,
                                        "No internet connection",
                                        Toast.LENGTH_SHORT,
                                        true
                                    ).show()
                                }

                                true
                            }

                            R.id.action_backup -> {
                                if (networkViewModel.isConnected.value == true) {
                                    if (SharedPrefManager.getUserUID()?.isEmpty() == false) {
                                        Toasty.info(
                                            this,
                                            "This feature will available soon",
                                            Toast.LENGTH_SHORT,
                                            true
                                        ).show()
                                    } else {
                                        Toasty.warning(
                                            this,
                                            "Login to Backup",
                                            Toast.LENGTH_SHORT,
                                            true
                                        ).show()
                                    }
                                } else {
                                    Toasty.error(
                                        this,
                                        "No internet connection",
                                        Toast.LENGTH_SHORT,
                                        true
                                    ).show()
                                }

                                true
                            }

                            else -> false
                        }
                    }
                    popupMenu.show()
                } else {
                    Log.e("PopupMenu", "menu_icon view not found")
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
