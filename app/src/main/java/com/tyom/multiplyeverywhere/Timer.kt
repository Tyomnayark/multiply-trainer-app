package com.tyom.multiplyeverywhere

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Timer(private var time: Long) {
    suspend fun startTimer() {
        delay(time * 1000)
    }
}