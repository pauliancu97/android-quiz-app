package com.paul.android.quizapp.network.service

import com.paul.android.quizapp.network.models.JsonCategories
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuizRetrofitService {
    @GET("api_category.php")
    suspend fun getCategories(): JsonCategories

    companion object {
        const val BASE_URL = "https://opentdb.com"
    }
}