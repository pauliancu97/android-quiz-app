package com.paul.android.quizapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paul.android.quizapp.entities.Answer

@Dao
interface AnswerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(answers: List<Answer>): List<Long>
}