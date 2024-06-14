package com.example.freshgrade.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.freshgrade.R
import com.example.freshgrade.databinding.ActivityMainBinding
import com.example.freshgrade.ui.authorization.AuthActivity
import com.example.freshgrade.ui.main.profile.ProfileFragment
import com.example.freshgrade.ui.util.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)


        // Visibility of navbar only in 3 fragments
        val visibleDestinations = setOf(
            R.id.navigation_history,
            R.id.navigation_article,
            R.id.navigation_camera
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in visibleDestinations) {
                navView.visibility = View.VISIBLE
            } else {
                navView.visibility = View.GONE
            }
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        // Initialize the ViewModel using the custom factory
        val factory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        mainViewModel.getUser().observe(this@MainActivity) { user ->
            if (user.isLogin) {
                Toast.makeText(this, R.string.have_permision, Toast.LENGTH_SHORT).show()
                Log.d("MainActivity", "User: $user")

            } else {
                Toast.makeText(this, R.string.dont_have_permision, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }

        toProfile()
    }

    private fun toProfile() {
        binding.profileImageView.setOnClickListener {
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            when (navController.currentDestination?.id) {
                R.id.navigation_camera -> navController.navigate(R.id.action_navigation_camera_to_profileFragment)
                R.id.navigation_article -> navController.navigate(R.id.action_navigation_article_to_profileFragment)
                R.id.navigation_history -> navController.navigate(R.id.action_navigation_history_to_profileFragment)
            }
        }
    }



}