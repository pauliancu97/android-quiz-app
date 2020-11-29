package com.paul.android.quizapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paul.android.quizapp.database.entities.Answer
import com.paul.android.quizapp.database.entities.QuestionAnswerCrossReference
import javax.inject.Singleton

@Singleton
@Dao
interface AnswerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(answers: List<Answer>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(answer: Answer): Long

    @Query("SELECT * FROM ${Answer.TABLE_NAME} " +
            "JOIN ${QuestionAnswerCrossReference.TABLE_NAME} ON ${Answer.ID} = ${QuestionAnswerCrossReference.ANSWER_ID} " +
            "WHERE ${QuestionAnswerCrossReference.QUESTION_ID} = :questiondId")
    suspend fun getAnswersForQuestion(questiondId: Long): List<Answer>
}