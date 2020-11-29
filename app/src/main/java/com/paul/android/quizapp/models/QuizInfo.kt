package com.paul.android.quizapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizInfo(
    val questions: List<QuestionModel>,
    val timeLimitPerQuestion: Int
) : Parcelable