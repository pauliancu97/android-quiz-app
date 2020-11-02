package com.paul.android.quizapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.paul.android.quizapp.entities.QuestionAnswerCrossReference

@Dao
interface QuestionAnswerDao {
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(questionAnswer: QuestionAnswerCrossReference): Long
}