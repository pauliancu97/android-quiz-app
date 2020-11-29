package com.paul.android.quizapp.ui.playgame

import com.paul.android.quizapp.models.QuestionModel

data class PlayingQuizState(
    val questions: List<QuestionModel> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val timeLimitPerQuestion: Int = 0,
    val timeLeftForCurrentQuestion: Int = 0,
    val chosenAnswer: String? = null,
    val score: Int = 0,
    val questionResult: QuestionResult = QuestionResult.Waiting,
    val timeLeftForShowingResult: Int = 0
)

enum class QuestionResult {
    Waiting,
    Correct,
    Incorrect,
    TimeExpired
}