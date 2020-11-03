package com.paul.android.quizapp.network.service

import com.paul.android.quizapp.network.models.JsonCategories
import com.paul.android.quizapp.network.models.JsonQuestionsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface QuizRetrofitService {
    @GET("api_category.php")
    suspend fun getCategories(): JsonCategories

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Long?,
        @Query("difficulty") difficulty: String?,
        @Query("type") type: String?
    ): JsonQuestionsResponse

    companion object {
        const val BASE_URL = "https://opentdb.com"
    }
}