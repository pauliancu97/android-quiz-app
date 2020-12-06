package com.paul.android.quizapp.realtimedatabase

import com.google.firebase.database.FirebaseDatabase
import com.paul.android.quizapp.models.FinishQuizInfo
import com.paul.android.quizapp.models.QuestionModel
import com.paul.android.quizapp.models.QuizDescriptionModel
import com.paul.android.quizapp.users.UserProvider
import com.paul.android.quizapp.utils.asFlow
import com.paul.android.quizapp.utils.getReferenceByPath
import com.paul.android.quizapp.utils.getValueOnceSuspend
import com.paul.android.quizapp.utils.setValueSuspend
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class QuizFirebaseDao @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val userProvider: UserProvider
){

    companion object {
        private const val KEY_CHOSEN_ANSWER = "chosen_answer"
        private const val KEY_QUIZ_QUESTION = "quiz_question"
        private const val KEY_QUIZES = "quizes"
        private const val KEY_DATE = "date"
        private const val KEY_NUM_QUESTIONS = "num_questions"
        private const val KEY_TIME_LIMIT = "time_limit"
        private const val KEY_SCORE = "score"
        private const val KEY_QUESTIONS = "questions"
        private const val KEY_USER = "user"
        private const val DATE_FORMAT = "dd/MM/yyyy HH:mm"
    }

    private fun Pair<QuestionModel, String?>.getQuizQuestionMap(): Map<String, Any?>? {
        val (questionModel, chosenAnswer) = this
        val questionFirebaseId = questionModel.firebaseId ?: return null
        return mapOf(
            questionFirebaseId to true,
            KEY_CHOSEN_ANSWER to chosenAnswer
        )
    }

    private fun getQuizDescriptionModelFromMap(map: Map<String, Any?>): QuizDescriptionModel {
        val dateFormatter = SimpleDateFormat(DATE_FORMAT)
        val date = (map[KEY_DATE] as? String)?.let {
            try {
                dateFormatter.parse(it)
            } catch (ex: Throwable) {
                null
            }
        } ?: throw IllegalArgumentException()
        val numOfQuestions = (map[KEY_NUM_QUESTIONS] as? Long)?.toInt() ?: throw IllegalArgumentException()
        val score = (map[KEY_SCORE] as? Long)?.toInt() ?: throw IllegalArgumentException()
        val timeLimit = (map[KEY_TIME_LIMIT] as? Long)?.toInt() ?: throw IllegalArgumentException()
        return QuizDescriptionModel(
            date = date,
            numOfQuestions = numOfQuestions,
            score = score,
            timeLimit = timeLimit
        )
    }

    private suspend fun getQuizDescriptionModel(quizUid: String): QuizDescriptionModel {
        val quizReference = firebaseDatabase.reference.getReferenceByPath("quizes/$quizUid")
        val mapOfQuizDescriptionModel = quizReference.getValueOnceSuspend<Map<String, Any?>>()
        return getQuizDescriptionModelFromMap(mapOfQuizDescriptionModel)
    }

    fun getUserQuizesDescriptionModels(userUid: String): Flow<List<QuizDescriptionModel>> =
        firebaseDatabase.reference.getReferenceByPath("users/$userUid/quizes")
            .asFlow<Map<String, Any?>>()
            .map { it.keys.toList() }
            .map { quizesUids -> quizesUids.map { getQuizDescriptionModel(it) } }

    private suspend fun addQuizQuestion(quizQuestion: Map<String, Any?>): String? {
        val reference = firebaseDatabase.reference.child(KEY_QUIZ_QUESTION).push()
        reference.setValueSuspend(quizQuestion)
        return reference.key
    }

    suspend fun addQuiz(finishQuizInfo: FinishQuizInfo): String? =
        with(finishQuizInfo) {
            val userId = userProvider.getUser()?.uid ?: return@with null
            val result = HashMap<String, Any?>()
            val dateFormatter = SimpleDateFormat(DATE_FORMAT)
            result[KEY_DATE] = dateFormatter.format(Date())
            result[KEY_NUM_QUESTIONS] = quizInfo.questions.size
            result[KEY_TIME_LIMIT] = quizInfo.timeLimitPerQuestion
            result[KEY_SCORE] = score
            result[KEY_USER] = userId
            val quizQuestionsIds = quizInfo.questions.zip(chosenAnswers)
                .mapNotNull { it.getQuizQuestionMap() }
                .mapNotNull { addQuizQuestion(it) }
            val questionsMap = quizQuestionsIds
                .map { Pair(it, true) }
                .toMap()
            result[KEY_QUESTIONS] = questionsMap
            val reference = firebaseDatabase.reference.child(KEY_QUIZES).push()
            reference.setValueSuspend(result)
            val quizId = reference.key ?: return@with null
            firebaseDatabase.reference.updateChildren(
                mapOf(
                    "/users/$userId/$KEY_QUIZES/$quizId" to true
                )
            )
            quizId
        }
}