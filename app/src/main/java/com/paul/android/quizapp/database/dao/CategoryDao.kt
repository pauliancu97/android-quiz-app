package com.paul.android.quizapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paul.android.quizapp.database.entities.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(categories: List<Category>): List<Long>

    @Query("SELECT * FROM ${Category.TABLE_NAME}")
    fun getAll(): List<Category>
}