package com.paul.android.quizapp.repository

import com.paul.android.quizapp.database.dao.DifficultyDao
import com.paul.android.quizapp.database.entities.Difficulty
import javax.inject.Inject

class DifficultyRepository @Inject constructor(
    private val difficultyDao: DifficultyDao
) {

    suspend fun insertInitialDifficulties() {
        val difficultiesNames = arrayOf("Easy", "Medium", "Hard")
        val difficultiesEntities = difficultiesNames.map { Difficulty(name = it) }
        difficultyDao.insertAll(difficultiesEntities)
    }

}