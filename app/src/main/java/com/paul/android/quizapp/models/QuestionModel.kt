package com.paul.android.quizapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionModel(
    val questionText: String,
    val category: String,
    val difficulty: String,
    val possibleAnswers: List<String>,
    val correctAnswer: String
): Parcelable