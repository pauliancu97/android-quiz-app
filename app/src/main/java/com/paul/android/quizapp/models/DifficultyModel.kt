package com.paul.android.quizapp.models

enum class DifficultyModel(val id: String) {
    AnyType(""),
    Easy("easy"),
    Medium("medium"),
    Hard("hard");

    fun toParameter() = id.takeIf { it.isNotBlank() }
}