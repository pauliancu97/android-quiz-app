package com.paul.android.quizapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.paul.android.quizapp.database.entities.Answer

@Dao
interface AnswerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(answers: List<Answer>): List<Long>
}