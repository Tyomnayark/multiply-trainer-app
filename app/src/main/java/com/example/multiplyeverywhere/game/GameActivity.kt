package com.example.multiplyeverywhere.game

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.SharedPreferencesHelper
import com.example.multiplyeverywhere.database.DataBaseController
import com.example.multiplyeverywhere.ui.trainer.FailActivity
import com.example.multiplyeverywhere.ui.trainer.WInActivity

class GameActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()

        var round = 0
        var responseReceived = false
        var answer = ""
        var rightAnswer = ""
        var lives = 2

        val nextButton = findViewById<Button>(R.id.next_button)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val questionText = findViewById<TextView>(R.id.question_text)

       rightAnswer =  createGame(button1,button2,button3,button4,questionText)
//        progressBar.setBackgroundColor(getResources().getColor(R.color.custom_light_green))

        nextButton.setOnClickListener {
            if (responseReceived) {
               rightAnswer = createGame(button1,button2,button3,button4,questionText)
                round++
                progressBar.setProgress(round*10)
                responseReceived = false
            }
            if (round == 10){
                creditPoints(lives,round)
                    val winActivity = Intent(this, WInActivity::class.java)
                    startActivity(winActivity)
                    finish()
            }
        }

        button1.setOnClickListener {
            if (!responseReceived){
                responseReceived = true
                answer = button1.text.toString()
                if (answer.equals(rightAnswer)){
                    button1.setBackgroundColor(getResources().getColor(R.color.custom_light_green))
                } else {
                    button1.setBackgroundColor(Color.RED)
                    lives--
                    if (lives == 0){
                        creditPoints(lives,round)
                        val failActivity = Intent(this, FailActivity::class.java)
                        startActivity(failActivity)
                        finish()
                    }
                }
            }
        }
        button2.setOnClickListener {
            if (!responseReceived){
                responseReceived = true
                answer = button2.text.toString()
                if (answer.equals(rightAnswer)){
                    button2.setBackgroundColor(getResources().getColor(R.color.custom_light_green))
                } else {
                    button2.setBackgroundColor(Color.RED)
                    lives--
                    if (lives == 0){
                        creditPoints(lives,round)
                        val failActivity = Intent(this, FailActivity::class.java)
                        startActivity(failActivity)
                        finish()
                    }
                }
            }
        }
        button3.setOnClickListener {
            if (!responseReceived){
                responseReceived = true
                answer = button3.text.toString()
                if (answer.equals(rightAnswer)){
                    button3.setBackgroundColor(getResources().getColor(R.color.custom_light_green))
                } else {
                    button3.setBackgroundColor(Color.RED)
                    lives--
                    if (lives == 0){
                        creditPoints(lives,round)
                        val failActivity = Intent(this, FailActivity::class.java)
                        startActivity(failActivity)
                        finish()
                    }
                }
            }
        }
        button4.setOnClickListener {
            if (!responseReceived){
                responseReceived = true
                answer = button4.text.toString()
                if (answer.equals(rightAnswer)){
                    button4.setBackgroundColor(getResources().getColor(R.color.custom_light_green))
                } else {
                    button4.setBackgroundColor(Color.RED)
                    lives--
                    if (lives == 0){
                        creditPoints(lives,round)
                        val failActivity = Intent(this, FailActivity::class.java)
                        startActivity(failActivity)
                        finish()
                    }
                }
            }
        }
    }
    private fun createGame(button1: Button, button2: Button, button3: Button, button4: Button, questionText: TextView) :String{
        val minRange = 2
        val maxRange = 9

        var num1 = (Math.random() * (maxRange - minRange + 1) + minRange).toInt()
        var num2 = (Math.random() * (maxRange - minRange + 1) + minRange).toInt()


        var answers = ArrayList<Int>()

        answers.add(num1*num2)

        while(answers.size!=4){
            val minRange = 1
            val maxRange = num1 * num2 * 1.5
            val randomInt = (Math.random() * (maxRange - minRange + 1) + minRange).toInt()
            if (randomInt==0){
                continue
            }
            if (answers.contains(randomInt)){
              continue
            }

            answers.add(randomInt)
        }
        answers.shuffle()

        button1.setText(answers.get(0).toString())
        button1.setBackgroundColor(getResources().getColor(R.color.text_color_dark))

        button2.setText(answers.get(1).toString())
        button2.setBackgroundColor(getResources().getColor(R.color.text_color_dark))

        button3.setText(answers.get(2).toString())
        button3.setBackgroundColor(getResources().getColor(R.color.text_color_dark))

        button4.setText(answers.get(3).toString())
        button4.setBackgroundColor(getResources().getColor(R.color.text_color_dark))

        questionText.setText(num1.toString()+" x "+ num2.toString())

        return (num1*num2).toString()
    }
    private fun creditPoints(lives: Int, round: Int){
        val db = DataBaseController(this, null)
        var userName = SharedPreferencesHelper(this).getUserName()
        val user = db.getUserByName(userName)
        val points = (10-round) * 200 + lives * 100 + user!!.level
        db.updateUserPoints(userName, points )
    }
}