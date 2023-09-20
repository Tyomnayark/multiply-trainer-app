package com.example.multiplyeverywhere.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.SharedPreferencesHelper
import com.example.multiplyeverywhere.User
import com.example.multiplyeverywhere.database.DataBaseController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

lateinit var mediaPlayerWinSound : MediaPlayer

class WInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val preferences = SharedPreferencesHelper(this)
        val isSoundEnabled =  preferences.getSoundSetttings().toBoolean()

        mediaPlayerWinSound = MediaPlayer.create(this, R.raw.win)
        mediaPlayerWinSound.setOnCompletionListener{
                mp ->
            mp.stop()
        }
        if (isSoundEnabled) {
            mediaPlayerWinSound.start()
        }
        val rounds = intent.getIntExtra("rounds", 0)
        var lives = intent.getIntExtra("lives", 0)

        val db = DataBaseController(this, null)
        var userName = SharedPreferencesHelper(this).getUserName()
        val user = db.getUserByName(userName)

        lives = 3-lives

        var currentPoints = (rounds-lives) * 55

        if (user!!.points==0){
            addLastSixDays(db,user)
        }
        if (lives==0) {
            db.addScoreRecord(userName, getCurrentDate(), currentPoints, 0, 0, 1)
        }else
        {
            db.addScoreRecord(userName, getCurrentDate(), currentPoints, 0, 1, 0)
        }


        val points = currentPoints + user!!.points

        val startLevel = user!!.level
        var F = countUserLevel(points)
            F+=1.0
        var finalLevel = F.toInt()

        val startProgress = countProgress(countUserLevel(user.points)+1.0, user.level)
        val endProgress = countProgress(F, finalLevel)

        val levelText = findViewById<TextView>(R.id.level_text)
        levelText.setText(startLevel.toString())

        val pointsText = findViewById<TextView>(R.id.points_text)
        val newPointsText = "+ " + (currentPoints.toString() + " " + getString(R.string.points_text))
        pointsText.text = newPointsText

        val scalePointsAnimator = ValueAnimator.ofFloat(1f, 1.2f)
        scalePointsAnimator.duration = 500
        scalePointsAnimator.repeatCount = 1
        scalePointsAnimator.repeatMode = ValueAnimator.REVERSE

        scalePointsAnimator.addUpdateListener { animation ->
            val scale = animation.animatedValue as Float
            pointsText.scaleX = scale
            pointsText.scaleY = scale
        }

        val pointsAnimatorSet = AnimatorSet()
        pointsAnimatorSet.playSequentially(scalePointsAnimator)
        pointsAnimatorSet.start()


        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        if (startLevel!=finalLevel) {

            val levelText = findViewById<TextView>(R.id.level_text)
            val newText = finalLevel.toString()

            val scaleAnimator = ValueAnimator.ofFloat(1f, 1.5f)
            scaleAnimator.duration = 500
            scaleAnimator.repeatCount = 1
            scaleAnimator.repeatMode = ValueAnimator.REVERSE

            scaleAnimator.addUpdateListener { animation ->
                val scale = animation.animatedValue as Float
                levelText.scaleX = scale
                levelText.scaleY = scale
            }

            val textChangeAnimator = ValueAnimator.ofObject(TypeEvaluator<String> { fraction, startValue, endValue ->
                val start = startValue.toString()
                val end = endValue.toString()
                val length = (start.length * (1 - fraction) + end.length * fraction).toInt()
                val result = StringBuilder(length)

                for (i in 0 until length) {
                    val cStart = if (i < start.length) start[i] else ' '
                    val cEnd = if (i < end.length) end[i] else ' '
                    result.append((cStart + ((cEnd - cStart) * fraction).toInt()).toChar())
                }
                result.toString()
            }, levelText.text, newText)

            textChangeAnimator.duration = 500

            textChangeAnimator.addUpdateListener { animation ->
                val animatedText = animation.animatedValue as String
                levelText.text = animatedText
            }

            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(scaleAnimator, textChangeAnimator)
            animatorSet.start()

            val progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", startProgress, 100)
            progressAnimator.interpolator = LinearInterpolator()
            progressAnimator.duration = (100-startProgress)*50L
            progressAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {

                    val animator = ObjectAnimator.ofInt(progressBar, "progress", 0, endProgress)
                    progressAnimator.interpolator = LinearInterpolator()
                    animator.duration = endProgress*50L
                    animator.start()
                }
            })
            progressAnimator.start()


        }else {
            val animator = ObjectAnimator.ofInt(progressBar, "progress", startProgress, endProgress)
            animator.interpolator = LinearInterpolator()
            animator.duration = (endProgress-startProgress)*50L
            animator.start()
        }

        db.updateUserPoints(userName, points )
        db.updateUserLevel(userName, finalLevel)

        val continueButton = findViewById<Button>(R.id.continue_button)
        continueButton.setOnClickListener { 
            finish()
        }
        val imageView = findViewById<ImageView>(R.id.cat_gif_win_activity)
        val resourceId = R.raw.happy_cat2
        Glide.with(this).asGif().load(resourceId).into(imageView)

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

    private fun countProgress(F: Double, level: Int ) : Int {
     return  ((F-(level.toDouble()))*100).toInt()
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
        mediaPlayerWinSound.release()
    }
}
