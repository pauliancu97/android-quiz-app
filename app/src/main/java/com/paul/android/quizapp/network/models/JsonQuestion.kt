package com.paul.android.quizapp.network.models

import com.squareup.moshi.Json

data class JsonQuestion(
    @field:Json(name = "category") val category: String,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "difficulty") val difficulty: String,
    @field:Json(name = "question") val question: String,
    @field:Json(name = "correct_answer") val correctAnswer: String,
    @field:Json(name = "incorrect_answers") val incorrectAnswers: List<String>
)