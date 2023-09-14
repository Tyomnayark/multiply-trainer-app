package com.example.multiplyeverywhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.multiplyeverywhere.database.*
import java.text.SimpleDateFormat
import java.util.Date

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val textArea = findViewById<EditText>(R.id.user_name)
        val singInButton = findViewById<Button>(R.id.sign_in_button)

        val db = DataBaseController(this, null)

        singInButton.setOnClickListener {
                val name = textArea.text.toString().trim()

               if( !isValidName(name,db)){
                   return@setOnClickListener
               }
                    val user = User(name, 0, "cat", 1 )
                    db.addUser(user)

                    val preferences = SharedPreferencesHelper(this)
                    preferences.setUserName(name)

                    val mainActivity = Intent(this, MainActivity::class.java)
                    startActivity(mainActivity)
                    this.finish()
        }
    }

    // TODO: fix resources
   private fun isValidName(name: String, db: DataBaseController) : Boolean {
        if (name == ""){
            Toast.makeText(this, R.string.login_error_empty, Toast.LENGTH_SHORT).show()
            return false
        }
        if (name.length > 20){
            Toast.makeText(this, R.string.login_error_length, Toast.LENGTH_SHORT).show()
            return false
        }
       if (db.getUserByName(name)!=null){
           Toast.makeText(this, R.string.login_error_user_exist, Toast.LENGTH_SHORT).show()
           return false
       }

        return true
    }
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MM.dd")
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}
