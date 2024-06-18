package com.nemanja02.rma.quiz

import com.nemanja02.rma.quiz.model.AnswerOption
import com.nemanja02.rma.quiz.model.Question

interface QuizContract {

    data class QuizUiState(
        val questions: List<Question> = emptyList(),
        val currentQuestionIndex: Int = 0,
        val score: Float = 0f,
        val updating: Boolean = false,
        val error: Exception? = null,
        val selectedOption: AnswerOption? = null,
        val isOptionCorrect: Boolean? = null,
        val timeRemaining: Long = 300000L // 5 minutes in milliseconds
    )
}