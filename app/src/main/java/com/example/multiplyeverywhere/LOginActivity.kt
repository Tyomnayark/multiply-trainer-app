package com.example.multiplyeverywhere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LOginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val textArea = findViewById<EditText>(R.id.editText)
        val singInButton = findViewById<Button>(R.id.signInButton)

        singInButton.setOnClickListener {
            val name = textArea.text.toString().trim()
            if (name!= ""){
                val user = User(name, 0)
            }
            else{
                Toast.makeText(this, R.string.login_error.toString(),Toast.LENGTH_SHORT)
            }
        }
    }
}