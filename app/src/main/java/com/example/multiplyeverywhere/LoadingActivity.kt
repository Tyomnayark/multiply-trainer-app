package com.example.multiplyeverywhere

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.multiplyeverywhere.database.DataBaseController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoadingActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        supportActionBar?.hide()

        GlobalScope.launch(Dispatchers.IO) {
            delay(1500)

            val db = DataBaseController(this@LoadingActivity, null)
            val databaseExists = db.doesDatabaseExist(this@LoadingActivity, "usersdb")

            val intent = if (!databaseExists) {
                Intent(this@LoadingActivity, LoginActivity::class.java)
            } else {
                Intent(this@LoadingActivity, MainActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
    }
}