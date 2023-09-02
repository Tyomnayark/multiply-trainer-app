package com.example.multiplyeverywhere.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.multiplyeverywhere.User

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "aaaaa"
    }
    val text: LiveData<String> = _text
}