package com.paul.android.quizapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paul.android.quizapp.database.entities.Difficulty
import javax.inject.Singleton

@Singleton
@Dao
interface DifficultyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(difficulties: List<Difficulty>): List<Long>

    @Query("SELECT * FROM ${Difficulty.TABLE_NAME}")
    suspend fun getAll(): List<Difficulty>

    @Query("SELECT ${Difficulty.ID} FROM ${Difficulty.TABLE_NAME} WHERE ${Difficulty.NAME} = :name")
    suspend fun getDifficultyIdWithName(name: String): Long

    @Query("SELECT COUNT(*) FROM ${Difficulty.TABLE_NAME}")
    suspend fun getNumberOfDifficulties(): Long

    @Query("SELECT * FROM ${Difficulty.TABLE_NAME} WHERE ${Difficulty.ID} = :difficultyId")
    suspend fun getDifficulty(difficultyId: Long): Difficulty
}