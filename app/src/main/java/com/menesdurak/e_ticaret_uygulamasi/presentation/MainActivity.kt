package com.menesdurak.e_ticaret_uygulamasi.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavMenu)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> navController.navigate(R.id.homeFragment)

                R.id.categories -> navController.navigate(R.id.categoriesFragment)

                R.id.cart -> navController.navigate(R.id.cartFragment)

                R.id.favorites -> navController.navigate(R.id.favoritesFragment)

                R.id.user -> navController.navigate(R.id.userLogInFragment)
            }
            true
        }
    }

//    override fun onBackPressed() {
//        if (binding.bottomNavMenu.selectedItemId != R.id.home) {
//            binding.bottomNavMenu.selectedItemId = R.id.home
//            navController.navigate(R.id.homeFragment)
//        } else {
//            finish()
//        }
//
//    }
}