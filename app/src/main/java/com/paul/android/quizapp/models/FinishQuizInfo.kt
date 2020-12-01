package com.paul.android.quizapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FinishQuizInfo(
    val quizInfo: QuizInfo,
    val score: Int,
    val chosenAnswers: List<String?> = emptyList()
) : Parcelable