package com.example.multiplyeverywhere

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.multiplyeverywhere.database.DataBaseController
import com.example.multiplyeverywhere.databinding.ActivityMainBinding
import com.example.multiplyeverywhere.ui.dashboard.DashboardFragment
import com.example.multiplyeverywhere.ui.home.HomeFragment
import com.example.multiplyeverywhere.ui.home.HomeViewModel
import com.example.multiplyeverywhere.ui.notifications.NotificationsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loadingScreen = Intent( this, LoadingActivity::class.java)
        startActivity(loadingScreen)

        val db = DataBaseController(this, null)
        val databaseExists = db.doesDatabaseExist(this, "users")

        if (!databaseExists) {
        val registration = Intent(this, LoginActivity::class.java)
            startActivity(registration)
        }
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = NavHostFragment.findNavController(supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)!!)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
    }

}
