package com.paul.android.quizapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.paul.android.quizapp.database.entities.QuestionAnswerCrossReference
import javax.inject.Singleton

@Singleton
@Dao
interface QuestionAnswerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(questionAnswer: QuestionAnswerCrossReference): Long
}