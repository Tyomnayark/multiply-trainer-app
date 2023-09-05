package com.example.multiplyeverywhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.multiplyeverywhere.database.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val textArea = findViewById<EditText>(R.id.user_name)
        val singInButton = findViewById<Button>(R.id.sign_in_button)


        singInButton.setOnClickListener {
                val name = textArea.text.toString().trim()
                if (name != "") {
                    val user = User(name, 1)
                    val db = DataBaseController(this, null)
                    db.addUser(user)
                    val preferences = SharedPreferencesHelper(this)
                    preferences.setUserName(name)

                    val mainActivity = Intent(this, MainActivity::class.java)
                    startActivity(mainActivity)
                    this.finish()
                } else {
                    Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
                }
        }
    }
}
