package com.tyom.multiplyeverywhere.game

import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tyom.multiplyeverywhere.R
import com.tyom.multiplyeverywhere.SharedPreferencesHelper
import com.tyom.multiplyeverywhere.User
import com.tyom.multiplyeverywhere.database.DataBaseController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class FailActivity : AppCompatActivity() {
    lateinit var mediaPlayerFailSound: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fail)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val preferences = SharedPreferencesHelper(this)
        val isSoundEnabled =  preferences.getSoundSetttings().toBoolean()

        val rounds = intent.getIntExtra("rounds", 0)
        var lives = intent.getIntExtra("lives", 0)

        mediaPlayerFailSound =MediaPlayer.create(this, R.raw.game_over)
        mediaPlayerFailSound.setOnCompletionListener{
                mp ->
            mp.stop()

        }
        if (isSoundEnabled) {
            mediaPlayerFailSound.start()
        }
        val db = DataBaseController(this, null)
        var userName = SharedPreferencesHelper(this).getUserName()
        val user = db.getUserByName(userName)

        lives = 2-lives

        var currentPoints = (rounds-lives) * 50
        if (user!!.points==0){
            addLastSixDays(db,user)
        }

        db.addScoreRecord(userName, getCurrentDate(), currentPoints, 1, 0, 0)


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
    fun addLastSixDays(db: DataBaseController, user: User){
        val dateFormat = SimpleDateFormat("MM.dd")
        val currentDate = Date()
        val calendar = Calendar.getInstance()

        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        var yesterdayDate = calendar.time
        var yesterdayDateString = dateFormat.format(yesterdayDate)
        db.addScoreRecord(user.name, yesterdayDateString, 0,0,0,0)
        while (dateFormat.format(currentDate)!=yesterdayDateString){
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            yesterdayDate = calendar.time
            yesterdayDateString = dateFormat.format(yesterdayDate)
            db.addScoreRecord(user.name, yesterdayDateString, 0,0,0,0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerFailSound.release()
    }
}