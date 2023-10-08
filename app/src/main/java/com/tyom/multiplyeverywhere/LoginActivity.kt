package com.tyom.multiplyeverywhere

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.tyom.multiplyeverywhere.database.*
import java.text.SimpleDateFormat
import java.util.Date

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val textArea = findViewById<EditText>(R.id.user_name)
        val singInButton = findViewById<Button>(R.id.sign_in_button)

        val db = DataBaseController(this, null)

        singInButton.setOnClickListener {
                val name = textArea.text.toString().trim()

               if( !isValidName(name,db)){
                   return@setOnClickListener
               }
                    val user = User(name, 0, "cat", 1 ,0)
                    db.addUser(user)

                    val preferences = SharedPreferencesHelper(this)
                    preferences.setUserName(name)

                    val mainActivity = Intent(this, MainActivity::class.java)
                    startActivity(mainActivity)
                    this.finish()
        }
        val imageView = findViewById<ImageView>(R.id.cat_gif_login_activity)
        val resourceId = R.raw.lazy_cat
        Glide.with(this).asGif().load(resourceId).into(imageView)

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
