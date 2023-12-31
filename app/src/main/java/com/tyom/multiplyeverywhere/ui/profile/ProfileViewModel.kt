package com.tyom.multiplyeverywhere.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _userName = MutableLiveData<String>().apply {
        value = "User name"
    }
    val userName: LiveData<String> = _userName

    private val _userLevel = MutableLiveData<String>().apply {
        value = "Level"
    }
    val userLevel: LiveData<String> = _userLevel
    fun setText(userName: String, levelString: String , context: Context){
        var levelText =  levelString
        _userName.value = userName
        _userLevel.value = levelText
    }

}