package com.example.multiplyeverywhere

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
        val textArea = findViewById<EditText>(R.id.user_name)
        val singInButton = findViewById<Button>(R.id.sign_in_button)


        singInButton.setOnClickListener {
                val name = textArea.text.toString().trim()
                if (name != "") {
                    val user = User(name, 0)
                    val db = DataBaseController(this, null)
                    db.addUser(user)
                    val preferences = SharedPreferencesHelper(this)
                    preferences.setUserName(name)
                    val greetingMessage = getString(R.string.Greetings) + " " + name + "!"
                    Toast.makeText(this, greetingMessage, Toast.LENGTH_SHORT).show()
                    this.finish()
                } else {
                    Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
                }
        }
    }
}
