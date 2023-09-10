package com.example.multiplyeverywhere

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.multiplyeverywhere.database.*

class LoginActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val textArea = findViewById<EditText>(R.id.user_name)
        val singInButton = findViewById<Button>(R.id.sign_in_button)


        singInButton.setOnClickListener {
            val name = textArea.text.toString().trim()
            val db = DataBaseController(this, null)

               if( !isValidName(name,db)){
                   return@setOnClickListener
               }
                    val user = User(name, 1)
                    db.addUser(user)

                    val preferences = SharedPreferencesHelper(this)
                    preferences.setUserName(name)

                    val mainActivity = Intent(this, MainActivity::class.java)
                    mainActivity.putExtra(Constants.USER_NAME, name)
                    startActivity(mainActivity)
                    this.finish()
        }
    }
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
}
