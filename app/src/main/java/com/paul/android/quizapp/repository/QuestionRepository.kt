package com.paul.android.quizapp.repository

import android.util.Log
import com.paul.android.quizapp.database.Database
import com.paul.android.quizapp.database.dao.*
import com.paul.android.quizapp.database.entities.Answer
import com.paul.android.quizapp.database.entities.Question
import com.paul.android.quizapp.database.entities.QuestionAnswerCrossReference
import com.paul.android.quizapp.models.CategoryModel
import com.paul.android.quizapp.models.DifficultyModel
import com.paul.android.quizapp.models.QuestionTypeModel
import com.paul.android.quizapp.models.QuestionModel
import com.paul.android.quizapp.network.service.QuizRetrofitService
import com.paul.android.quizapp.realtimedatabase.QuestionsFirebaseDao
import java.lang.IllegalStateException
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val questionDao: QuestionDao,
    private val categoryDao: CategoryDao,
    private val difficultyDao: DifficultyDao,
    private val answerDao: AnswerDao,
    private val questionAnswerDao: QuestionAnswerDao,
    private val quizRetrofitService: QuizRetrofitService,
    private val questionsFirebaseDao: QuestionsFirebaseDao
) {
    suspend fun fetchQuestions(
        amount: Int,
        category: CategoryModel = CategoryModel.AnyCategoryModel,
        difficulty: DifficultyModel = DifficultyModel.AnyType,
        type: QuestionTypeModel = QuestionTypeModel.AnyType
    ) {
        val response = quizRetrofitService.getQuestions(
            amount,
            category.toParameter(),
            difficulty.toParameter(),
            type.toParameter()
        )
        val jsonQuestions = response.results
        jsonQuestions.forEach { jsonQuestion ->
            Log.i("QuestionRepository", jsonQuestion.toString())
            val categoryId = categoryDao.getCategoryIdWithName(jsonQuestion.category)
            val difficultyId = difficultyDao.getDifficultyIdWithName(jsonQuestion.difficulty)
            val correctAnswerId = answerDao.insert(Answer(text = jsonQuestion.correctAnswer))
            val incorrectAnswersIds = answerDao.insertAll(
                jsonQuestion.incorrectAnswers.map { Answer(text = it) }
            )
            val question = Question(
                text = jsonQuestion.question,
                difficultyId = difficultyId,
                categoryId = categoryId,
                correctAnswerId = correctAnswerId
            )
            val questionId = questionDao.insert(question)
            val questionAndAnswerCrossRefs = (incorrectAnswersIds + correctAnswerId)
                .map { QuestionAnswerCrossReference(questionId = questionId, answerId = it) }
            questionAnswerDao.insert(questionAndAnswerCrossRefs)
        }
    }

    suspend fun getAll(): List<QuestionModel> {
        val questionEntities = questionDao.getAll()
        return questionEntities
            .mapNotNull { questionEntity -> questionEntity.id?.let { Pair(it, questionEntity) } }
            .map { (questionId, questionEntity) ->
                val difficultyString = difficultyDao.getDifficulty(questionEntity.difficultyId).name
                val categoryString = categoryDao.getCategory(questionEntity.categoryId).name
                val possibleAnswers = answerDao.getAnswersForQuestion(questionId)
                val possibleAnswersStrings = possibleAnswers.map { it.text }
                val correctAnswerString = possibleAnswers.find { it.id == questionEntity.correctAnswerId }?.text ?: throw IllegalStateException()
                QuestionModel(
                    questionText = questionEntity.text,
                    category = categoryString,
                    difficulty = difficultyString,
                    possibleAnswers = possibleAnswersStrings,
                    correctAnswer = correctAnswerString
                )
            }
    }

    suspend fun addToRealtimeDatabase(questions: List<QuestionModel>) =
        questionsFirebaseDao.addQuestions(questions)


    suspend fun deleteAll() {
        questionDao.deleteAll()
    }
}