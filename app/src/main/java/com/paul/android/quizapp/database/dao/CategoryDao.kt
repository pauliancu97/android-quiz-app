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

    @Query("SELECT ${Category.ID} FROM ${Category.TABLE_NAME} WHERE ${Category.NAME} = :name")
    suspend fun getCategoryIdWithName(name: String): Long

    @Query("SELECT COUNT(*) FROM ${Category.TABLE_NAME}")
    suspend fun getNumberOfCategories(): Long

    @Query("SELECT * FROM ${Category.TABLE_NAME} WHERE ${Category.ID} = :categoryId")
    suspend fun getCategory(categoryId: Long): Category
}