package com.paul.android.quizapp.realtimedatabase

import com.google.firebase.database.FirebaseDatabase
import com.paul.android.quizapp.models.QuestionModel
import com.paul.android.quizapp.utils.setValueSuspend
import javax.inject.Inject

class QuestionsFirebaseDao @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {

    companion object {
        private const val QUESTIONS = "questions"
    }

    suspend fun addQuestions(questions: List<QuestionModel>) {
        val questionsReference = firebaseDatabase.reference.child(QUESTIONS)
        questions
            .map { it.toMap() }
            .forEach {
                questionsReference
                    .push()
                    .setValueSuspend(it)
            }
    }

}