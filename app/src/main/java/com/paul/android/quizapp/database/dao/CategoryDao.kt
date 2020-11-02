package com.paul.android.quizapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paul.android.quizapp.database.entities.Category
import javax.inject.Singleton

@Singleton
@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categories: List<Category>): List<Long>

    @Query("SELECT * FROM ${Category.TABLE_NAME}")
    suspend fun getAll(): List<Category>
}