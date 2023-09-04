package com.example.multiplyeverywhere.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.SharedPreferencesHelper
import com.example.multiplyeverywhere.User

class HomeViewModel : ViewModel() {

    private val _userName = MutableLiveData<String>().apply {
        value = "User name"
    }
    val userName: LiveData<String> = _userName

    private val _userLevel = MutableLiveData<String>().apply {
        value = "Level"
    }
    val userLevel: LiveData<String> = _userLevel
    fun setText(userName: String, levelString: String , context: Context){
        var levelText = context.getText(R.string.level_text).toString() + " " + levelString
        _userName.value = userName
        _userLevel.value = levelText
    }
}