package com.example.multiplyeverywhere.ui.trainer

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.SharedPreferencesHelper
import com.example.multiplyeverywhere.database.DataBaseController


class WInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)
        supportActionBar?.hide()

        val rounds = intent.getIntExtra("rounds", 0)
        val lives = intent.getIntExtra("lives", 0)

        val db = DataBaseController(this, null)
        var userName = SharedPreferencesHelper(this).getUserName()
        val user = db.getUserByName(userName)


        val points = (rounds-2+lives) * 50 + lives * 50 + user!!.level

        val startProgress = countProgress(user!!.level)
        val endProgress = countProgress(points)

        val startLevel = countUserLevel(user!!.level)
        val finalLevel = countUserLevel(points)

        val levelText = findViewById<TextView>(R.id.level_text)
        levelText.setText(startLevel.toString())

        val pointsText = findViewById<TextView>(R.id.points_text)
        val newPointsText = "+ " + (( rounds - 2 + lives) * 100 + lives * 50).toString() + " " + getString(R.string.points_text)
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

            var animator = ObjectAnimator.ofInt(progressBar, "progress", startProgress, 100)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.duration = (100-startProgress)*100L
            animator.start()

            val progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, endProgress)
            progressAnimator.interpolator = AccelerateDecelerateInterpolator()
            progressAnimator.duration = endProgress*100L
            progressAnimator.start()

        }else {
            val animator = ObjectAnimator.ofInt(progressBar, "progress", startProgress, endProgress)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.duration = (endProgress-startProgress)*100L
            animator.start()
        }

        db.updateUserPoints(userName, points )
    }
    private fun countUserLevel(points: Int? ) : Int {
        if (points!! < 1000){
            return 1
        } else if (points < 2500 ){
            return 2
        }else if (points < 4500){
            return 3
        }else if (points < 8000){
            return 4
        } else if (points < 16000){
            return 5
        }else if (points < 32000){
            return 6
        }
        return 0
    }
    private fun countProgress(points: Int? ) : Int {
        var p = 0
        if (points!! < 1000){
           p=1000
        } else if (points < 2500 ){
            p=2500
        }else if (points < 4500){
            p=4500
        }else if (points < 8000){
            p=8000
        } else if (points < 16000){
            p=16000
        }else if (points < 32000){
            p=32000
        }
        return points/(p/100)
    }
}
