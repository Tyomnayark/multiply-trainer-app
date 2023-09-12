package com.example.multiplyeverywhere.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.multiplyeverywhere.R

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()

        val button_1 = findViewById<Button>(R.id.button1)
        val button_2 = findViewById<Button>(R.id.button2)
        val button_3 = findViewById<Button>(R.id.button3)
        val button_4 = findViewById<Button>(R.id.button4)


    }
}