package com.paul.android.quizapp.network.models

import com.squareup.moshi.Json

data class JsonCategories(
    @field:Json(name = "trivia_categories") val categories: List<JsonCategory>
)
