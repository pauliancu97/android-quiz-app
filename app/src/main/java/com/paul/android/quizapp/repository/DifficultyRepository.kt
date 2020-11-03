package com.paul.android.quizapp.repository

import com.paul.android.quizapp.database.dao.DifficultyDao
import com.paul.android.quizapp.database.entities.Difficulty
import com.paul.android.quizapp.models.DifficultyModel
import javax.inject.Inject

class DifficultyRepository @Inject constructor(
    private val difficultyDao: DifficultyDao
) {

    suspend fun insertInitialDifficulties() {
        val difficultiesNames = DifficultyModel.values().mapNotNull { it.toParameter() }
        val difficultiesEntities = difficultiesNames.map { Difficulty(name = it) }
        difficultyDao.insertAll(difficultiesEntities)
    }

}