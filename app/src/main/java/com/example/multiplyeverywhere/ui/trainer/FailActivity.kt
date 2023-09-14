package com.example.multiplyeverywhere.ui.trainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.SharedPreferencesHelper
import com.example.multiplyeverywhere.database.DataBaseController
import java.text.SimpleDateFormat
import java.util.Date

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

        if (currentPoints!= 0) {
            db.addScoreRecord(userName, getCurrentDate(), currentPoints)
        }

        val points = currentPoints + user!!.points
        var F = countUserLevel(points)
        F+=1.0
        var finalLevel = F.toInt()

        db.updateUserPoints(userName, points )
        db.updateUserLevel(userName,finalLevel)

        val continueButton = findViewById<Button>(R.id.continue_button)
        continueButton.setOnClickListener {
            finish()
        }
    }
    private fun countUserLevel(points: Int?): Double {
        val a = 100.0
        val b = 900.0
        val c = points!!.toDouble() * -1.0

        val discriminant = b * b - 4 * a * c!!

        if (discriminant < 0) {
            return 0.0
        }
        val sqrtDiscriminant = Math.sqrt(discriminant)

        val f1 = (-b + sqrtDiscriminant) / (2 * a)
        val f2 = (-b - sqrtDiscriminant) / (2 * a)

        return if (f1 > 0) f1 else if (f2 > 0) f2 else 0.0
    }
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MM.dd")
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}