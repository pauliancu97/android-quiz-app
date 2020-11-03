package com.paul.android.quizapp.network.models

import com.squareup.moshi.Json

data class JsonQuestionsResponse(
    @field:Json(name = "response_code") val responseCode: Int,
    @field:Json(name = "results") val results: List<JsonQuestion>
)