package com.bignerdranch.android.geoquiz_2

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.*
import org.junit.Test

class QuizViewModelTest {

    @Test
    //providing correct question after starting app
    fun providesExpectedQuestionText() {
        val savedStateHandler = SavedStateHandle()
        val quizViewModel = QuizViewModel(savedStateHandler)
        assertEquals(R.string.question_australia, quizViewModel.currentQuestionText)
    }

    @Test
    //after last question follows the first question
    fun wrapsAroundQuestionBank() {
        val savedStateHandle = SavedStateHandle(mapOf(CURRENT_INDEX_KEY to 5))
        val quizViewModel = QuizViewModel(savedStateHandle)
        assertEquals(R.string.question_asia, quizViewModel.currentQuestionText)
        quizViewModel.moveToNext()
        assertEquals(R.string.question_australia, quizViewModel.currentQuestionText)
    }

    @Test
    fun currentQuestionAnswerIsTrueFirstStart() {
        val savedStateHandle = SavedStateHandle()
        val quizViewModel = QuizViewModel(savedStateHandle)
        assertTrue(quizViewModel.currentQuestionAnswer)
    }

    @Test
    fun currentQuestionAnswerIsFalseQuestionWithIndexTwo() {
        val savedStateHandle = SavedStateHandle(mapOf(CURRENT_INDEX_KEY to 2))
        val quizViewModel = QuizViewModel(savedStateHandle)
        assertFalse(quizViewModel.currentQuestionAnswer)
    }
}