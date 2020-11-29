package com.paul.android.quizapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paul.android.quizapp.database.entities.Question
import javax.inject.Singleton

@Singleton
@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(questions: List<Question>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(question: Question): Long

    @Query("DELETE FROM ${Question.TABLE_NAME}")
    suspend fun deleteAll()

    @Query("SELECT * FROM ${Question.TABLE_NAME}")
    suspend fun getAll(): List<Question>
}