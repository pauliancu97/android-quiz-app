package com.paul.android.quizapp.models

data class CreateQuizStateModel(
    val category: CategoryModel,
    val difficulty: DifficultyModel,
    val type: QuestionTypeModel,
    val questionsNumber: Int,
    val timeLimit: Int,
    val maxNumQuestions: Int,
    val minNumQuestions: Int,
    val maxTimeLimit: Int,
    val minTimeLimit: Int
) {
    companion object {
        const val DEFAULT_QUESTIONS_NUMBER = 10
        const val DEFAULT_TIME_LIMIT = 10
        const val MIN_QUESTIONS_NUMBER = 5
        const val MAX_QUESTIONS_NUMBER = 50
        const val MIN_TIME_LIMIT = 5
        const val MAX_TIME_LIMIT = 20
    }
}