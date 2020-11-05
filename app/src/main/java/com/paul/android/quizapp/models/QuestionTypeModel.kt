package com.paul.android.quizapp.models

import com.paul.android.quizapp.R

enum class QuestionTypeModel(val id: String, val stringId: Int) {
    AnyType("", R.string.any_label),
    MultipleChoices("multiple", R.string.multiple_choices_label),
    Boolean("boolean", R.string.true_false_label);

    fun toParameter() = id.takeIf { it.isNotBlank() }
}