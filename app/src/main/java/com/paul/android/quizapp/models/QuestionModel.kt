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
): Parcelable {

    companion object {
        private const val KEY_TEXT = "text"
        private const val KEY_CATEGORY = "category"
        private const val KEY_DIFFICULTY = "difficulty"
        private const val KEY_WRONG_ANSWERS = "wrong_answers"
        private const val KEY_ANSWER = "answer"
        private const val KEY_CORRECT_ANSWER = "correct_answer"

        fun fromMap(data: Map<String, Any?>): QuestionModel? {
            val questionText = (data[KEY_TEXT] as? String) ?: return null
            val category = (data[KEY_CATEGORY] as? String) ?: return null
            val difficulty = (data[KEY_DIFFICULTY] as? String) ?: return null
            val correctAnswer = (data[KEY_CORRECT_ANSWER] as? String) ?: return null
            val wrongAnswersMap = (data[KEY_WRONG_ANSWERS] as? Map<*, *>) ?: return null
            val wrongAnswers = wrongAnswersMap.values.filterIsInstance<String>()
            if (wrongAnswers.isEmpty()) {
                return null
            }
            val answers = listOf(correctAnswer) + wrongAnswers
            return QuestionModel(
                questionText = questionText,
                category = category,
                difficulty = difficulty,
                possibleAnswers = answers,
                correctAnswer = correctAnswer
            )
        }
    }

    fun toMap(): Map<String, Any?> {
        val result = HashMap<String, Any?>()
        result[KEY_TEXT] = questionText
        result[KEY_CATEGORY] = category
        result[KEY_DIFFICULTY] = difficulty
        result[KEY_CORRECT_ANSWER] = correctAnswer
        val wrongAnswers = possibleAnswers
            .filter { it != correctAnswer }
            .mapIndexed { index, wrongAnswer ->
                Pair("${KEY_ANSWER}_${index}", wrongAnswer)
            }
            .toMap()
        result[KEY_WRONG_ANSWERS] = wrongAnswers
        return result
    }
}