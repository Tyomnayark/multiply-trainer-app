package com.example.multiplyeverywhere

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

//        val recreated = intent.getBooleanExtra("recreated", false)
//
//        if (recreated) {
//            val myView = findViewById<View>(R.id.container)
//            performCircularRevealAnimation(myView)
//        }

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
    fun performCircularRevealAnimation(view: View) {
        val cx = view.width / 2
        val cy = view.height / 2
        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
        anim.duration = 1000
        anim.start()
    }

}
