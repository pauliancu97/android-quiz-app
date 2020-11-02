package com.paul.android.quizapp.di.modules

import android.content.Context
import androidx.room.Room
import com.paul.android.quizapp.database.Database
import com.paul.android.quizapp.database.dao.*
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    fun provideDatabase(context: Context): Database =
        Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            "database"
        ).build()

    @Provides
    fun provideCategoryDao(database: Database): CategoryDao =
        database.getCategoryDao()

    @Provides
    fun provideDifficultyDao(database: Database): DifficultyDao =
        database.getDifficultyDao()

    @Provides
    fun provideAnswerDao(database: Database): AnswerDao =
        database.getAnswerDao()

    @Provides
    fun provideQuestionDao(database: Database): QuestionDao =
        database.getQuestionDao()

    @Provides
    fun provideQuestionAnswerDao(database: Database): QuestionAnswerDao =
        database.getQuestionAndAnswerDao()
}