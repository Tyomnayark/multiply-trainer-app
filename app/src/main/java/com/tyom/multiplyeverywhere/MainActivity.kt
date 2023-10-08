package com.tyom.multiplyeverywhere

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale


class MainActivity : AppCompatActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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
        navView.itemIconTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.text_color))
    }

    override fun onRestart() {
        super.onRestart()
        val preferencesHelper = SharedPreferencesHelper(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        when (preferencesHelper.getTheme()) {
            "day"-> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            "night" ->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val language = preferencesHelper.getLanguage()
        val locale  = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)

        this@MainActivity.resources.updateConfiguration(configuration, this@MainActivity.resources.displayMetrics)

    }

    override fun onResume() {
        super.onResume()
        val preferencesHelper = SharedPreferencesHelper(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        when (preferencesHelper.getTheme()) {
            "day"-> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            "night" ->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val language = preferencesHelper.getLanguage()
        val locale  = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)

        this@MainActivity.resources.updateConfiguration(configuration, this@MainActivity.resources.displayMetrics)
    }
}
