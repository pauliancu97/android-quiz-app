package com.paul.android.quizapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paul.android.quizapp.database.entities.Difficulty

@Dao
interface DifficultyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(difficulties: List<Difficulty>): List<Long>

    @Query("SELECT * FROM ${Difficulty.TABLE_NAME}")
    fun getAll(): List<Difficulty>
}