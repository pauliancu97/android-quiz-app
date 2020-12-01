package com.paul.android.quizapp.realtimedatabase

import com.google.firebase.database.FirebaseDatabase
import com.paul.android.quizapp.models.QuestionModel
import com.paul.android.quizapp.utils.setValueSuspend
import java.lang.IllegalStateException
import javax.inject.Inject

class QuestionsFirebaseDao @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {

    companion object {
        private const val QUESTIONS = "questions"
    }

    suspend fun addQuestions(questions: List<QuestionModel>): List<QuestionModel> {
        val questionsReference = firebaseDatabase.reference.child(QUESTIONS)
        val questionFirebaseIds = questions
            .map { it.toMap() }
            .mapNotNull {
                val ref = questionsReference.push()
                ref.setValueSuspend(it)
                ref.key
            }
        if (questionFirebaseIds.size != questions.size) {
            throw IllegalStateException()
        }
        return questions.zip(questionFirebaseIds) { question, firebaseId ->
            question.copy(firebaseId = firebaseId)
        }
    }

}