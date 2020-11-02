package com.paul.android.quizapp.network.service

import com.paul.android.quizapp.network.models.JsonCategories
import retrofit2.http.GET

interface QuizRetrofitService {
    @GET("api_category.php")
    suspend fun getCategories(): JsonCategories
}