package com.example.multiplyeverywhere.ui.trainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.SharedPreferencesHelper
import com.example.multiplyeverywhere.database.DataBaseController

class FailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fail)
        supportActionBar?.hide()

        val rounds = intent.getIntExtra("rounds", 0)
        var lives = intent.getIntExtra("lives", 0)

        val db = DataBaseController(this, null)
        var userName = SharedPreferencesHelper(this).getUserName()
        val user = db.getUserByName(userName)

        lives = 2-lives

        var currentPoints = (rounds-lives) * 50

        val points = currentPoints + user!!.level

        db.updateUserPoints(userName, points )
        val continueButton = findViewById<Button>(R.id.continue_button)
        continueButton.setOnClickListener {
            finish()
        }
    }
}