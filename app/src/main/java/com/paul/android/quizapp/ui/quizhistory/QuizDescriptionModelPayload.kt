package com.paul.android.quizapp.ui.quizhistory

data class QuizDescriptionModelPayload(
    val hasDateChanged: Boolean = true,
    val hasScoreChanged: Boolean = true,
    val hasTimeLimitChanged: Boolean = true
)