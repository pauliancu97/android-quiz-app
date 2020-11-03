package com.paul.android.quizapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.paul.android.quizapp.database.entities.Answer
import javax.inject.Singleton

@Singleton
@Dao
interface AnswerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(answers: List<Answer>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(answer: Answer): Long
}