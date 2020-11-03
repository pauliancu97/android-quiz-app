package com.paul.android.quizapp.models

enum class QuestionTypeModel(val id: String) {
    AnyType(""),
    MultipleChoices("multiple"),
    Boolean("boolean");

    fun toParameter() = id.takeIf { it.isNotBlank() }
}