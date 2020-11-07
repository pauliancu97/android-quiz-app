package com.paul.android.quizapp.models

import android.os.Parcelable
import com.paul.android.quizapp.R
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class DifficultyModel(val id: String, val stringId: Int): Parcelable {
    AnyType("", R.string.any_label),
    Easy("easy", R.string.easy_label),
    Medium("medium", R.string.medium_label),
    Hard("hard", R.string.hard_label);

    fun toParameter() = id.takeIf { it.isNotBlank() }
}