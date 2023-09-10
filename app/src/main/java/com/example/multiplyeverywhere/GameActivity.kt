package com.example.multiplyeverywhere

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    private var userName: String? = null
    private var task: TextView? = null
    private var input: EditText? = null
    private var submitBtn: Button? = null
    private var progressBar: ProgressBar? = null
    private var score = 0
    private var num1 = 0
    private var num2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()

        userName = intent.getStringExtra(Constants.USER_NAME)
        task = findViewById(R.id.task)
        input  = findViewById(R.id.input)
        submitBtn = findViewById(R.id.submit)
        progressBar = findViewById(R.id.progressBar)

        setTask()

        submitBtn!!.setOnClickListener {
            val userAns = input!!.text.toString().trim().toInt()

            if (isAnswerCorrect()) {
                score += 1
            }

            progressBar!!.progress += 20

            setTask()

            if (progressBar!!.progress == 100) {
                Toast.makeText(this, "Your score is $score", Toast.LENGTH_SHORT).show()
                val mainActivity = Intent(this, MainActivity::class.java)
                mainActivity.putExtra(Constants.USER_NAME, userName)
                startActivity(mainActivity)
            }
        }
    }

    private fun isAnswerCorrect(): Boolean {
        val userAns = input!!.text.toString().trim().toInt()
        return num1*num2 == userAns
    }

    @SuppressLint("SetTextI18n")
    private fun setTask() {
        num1 = (0..10).random()
        num2 = (0..10).random()

        task!!.text = "$num1 * $num2"
    }
}