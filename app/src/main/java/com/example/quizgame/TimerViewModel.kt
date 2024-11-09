package com.example.quizgame

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {



    private var countDownTimer: CountDownTimer? = null

    private val _countdownText = MutableLiveData<String>()
    val countdownText : LiveData<String> = _countdownText


    fun startTimer() {
        val totalTimeInMills = QuestionActivity.time.toInt() * 60 * 1000L
        countDownTimer = object: CountDownTimer(totalTimeInMills,1000){
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val  minutes = seconds / 60
                val remainingSeconds = seconds % 60
                val formattedTime = String.format("%02d:%02d", minutes, remainingSeconds)
                _countdownText.value = formattedTime

            }

            override fun onFinish() {
                _countdownText.value = "00:00"
            }

        }.start()
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel() // Cancel timer on ViewModel cleared
    }



}