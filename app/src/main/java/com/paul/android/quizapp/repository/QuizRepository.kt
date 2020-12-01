package com.paul.android.quizapp.repository

import com.paul.android.quizapp.models.FinishQuizInfo
import com.paul.android.quizapp.realtimedatabase.QuizFirebaseDao
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizFirebaseDao: QuizFirebaseDao
){
    suspend fun addQuiz(finishQuizInfo: FinishQuizInfo) = quizFirebaseDao.addQuiz(finishQuizInfo)
}