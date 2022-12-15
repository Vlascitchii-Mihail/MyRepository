@file:Suppress("unused")

package com.bignerdranch.android.geoquiz_2

import android.util.Log
import androidx.lifecycle.ViewModel
import android.content.Intent
import androidx.lifecycle.SavedStateHandle

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
//    init { Log.d(TAG, "ViewModel instance created")}

    //it cals before destroying the app
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var currentIndex: Int
    //get data from some place after app's process is killed
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    var count = 0
    var isCheater = false
    private var answerIsTrue = false
    var data: Intent? = null
    var cheatTries = 0

    fun showAnswerText() : Int {
        return when {
        answerIsTrue -> R.string.true_button
        else -> R.string.false_button
        }
    }

    val currentQuestionAnswer: Boolean get() = questionBank[currentIndex].answer
    val currentQuestionText : Int get() = questionBank[currentIndex].textResId

    //move to next question
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    //move to previous question
    fun moveToPrev() {
        currentIndex = if (currentIndex < 0) 0
        else (currentIndex -1) % questionBank.size
    }
}