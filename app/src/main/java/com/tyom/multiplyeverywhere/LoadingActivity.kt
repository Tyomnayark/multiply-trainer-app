package com.tyom.multiplyeverywhere

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.AudioManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import com.tyom.multiplyeverywhere.database.DataBaseController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class LoadingActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        val preferencesHelper = SharedPreferencesHelper(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val db = DataBaseController(this@LoadingActivity, null)
        val databaseExists = db.doesDatabaseExist(this@LoadingActivity, "usersdb")

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
        var language : String = ""
        var locale : Locale? = null
        if (!databaseExists){
            locale = Locale.getDefault()
            language = locale.language
            preferencesHelper.setLanguage(language)
        }else{
            language = preferencesHelper.getLanguage()
            locale  = Locale(language)
            Locale.setDefault(locale)
        }

        val configuration = Configuration()
        configuration.setLocale(locale)

        this@LoadingActivity.resources.updateConfiguration(configuration, this@LoadingActivity.resources.displayMetrics)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        supportActionBar?.hide()

        GlobalScope.launch(Dispatchers.IO) {
            delay(1500)


            val intent = if ((!databaseExists) || preferencesHelper.getUserName()== "" ) {

                Intent(this@LoadingActivity, LoginActivity::class.java)
            } else {
                Intent(this@LoadingActivity, MainActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
    }

    override fun onRestart() {
        super.onRestart()
        val preferencesHelper = SharedPreferencesHelper(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val db = DataBaseController(this@LoadingActivity, null)
        val databaseExists = db.doesDatabaseExist(this@LoadingActivity, "usersdb")

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

        var language : String = ""
        var locale : Locale? = null
        if (!databaseExists){
            locale = Locale.getDefault()
            language = locale.language
            preferencesHelper.setLanguage(language)
        }else{
            language = preferencesHelper.getLanguage()
            locale  = Locale(language)
            Locale.setDefault(locale)
        }

        val configuration = Configuration()
        configuration.setLocale(locale)

        this@LoadingActivity.resources.updateConfiguration(configuration, this@LoadingActivity.resources.displayMetrics)
    }
}