package com.paul.android.quizapp.models

import android.os.Parcelable
import com.paul.android.quizapp.R
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class QuestionTypeModel(val id: String, val stringId: Int): Parcelable {
    AnyType("", R.string.any_label),
    MultipleChoices("multiple", R.string.multiple_choices_label),
    Boolean("boolean", R.string.true_false_label);

    fun toParameter() = id.takeIf { it.isNotBlank() }
}