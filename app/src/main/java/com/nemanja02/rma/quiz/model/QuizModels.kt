package com.nemanja02.rma.quiz.model

data class Question(
    val id: String,
    val type: Int,
    val questionText: String,
    val imageUrl: String?,
    val options: List<AnswerOption>
)

data class AnswerOption(
    val text: String,
    val imageUrl: String?,
    val isCorrect: Boolean
)