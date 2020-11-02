package com.paul.android.quizapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paul.android.quizapp.database.dao.*
import com.paul.android.quizapp.database.entities.*
import javax.inject.Singleton

@Singleton
@Database(
    entities = [
        Difficulty::class,
        Category::class,
        Answer::class,
        Question::class,
        QuestionAnswerCrossReference::class
    ],
    version = 1,
    exportSchema = false
)
abstract class Database: RoomDatabase() {
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getDifficultyDao(): DifficultyDao
    abstract fun getQuestionDao(): QuestionDao
    abstract fun getAnswerDao(): AnswerDao
    abstract fun getQuestionAndAnswerDao(): QuestionAnswerDao
}