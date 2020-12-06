package com.paul.android.quizapp.models

import java.util.*

data class QuizDescriptionModel(
    val date: Date,
    val numOfQuestions: Int,
    val score: Int,
    val timeLimit: Int
)