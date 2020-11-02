package com.paul.android.quizapp.repository

import com.paul.android.quizapp.database.dao.CategoryDao
import com.paul.android.quizapp.database.entities.Category
import com.paul.android.quizapp.network.service.QuizRetrofitService
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val quizRetrofitService: QuizRetrofitService
) {
    suspend fun initializeCategories() {
        val jsonCategories = quizRetrofitService.getCategories().categories
        val categories = jsonCategories.map { Category(id = it.id, name = it.name) }
        categoryDao.insertAll(categories)
    }
}