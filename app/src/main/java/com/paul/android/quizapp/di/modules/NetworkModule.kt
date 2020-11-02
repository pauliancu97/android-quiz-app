package com.paul.android.quizapp.di.modules

import com.paul.android.quizapp.network.service.QuizRetrofitService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideQuizRetrofitService(): QuizRetrofitService =
        Retrofit.Builder()
            .baseUrl(QuizRetrofitService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(QuizRetrofitService::class.java)
}