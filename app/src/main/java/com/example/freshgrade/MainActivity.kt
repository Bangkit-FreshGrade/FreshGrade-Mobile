package com.example.freshgrade

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.freshgrade.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        // visibilitas navbar hanya di 3 fragment
        val visibleDestinations = setOf(R.id.navigation_history, R.id.navigation_article, R.id.navigation_camera)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in visibleDestinations) {
                navView.visibility = View.VISIBLE
            }
            else navView.visibility = View.GONE
        }
        //
    }

}