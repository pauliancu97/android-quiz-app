package com.paul.android.quizapp.network.models

import com.squareup.moshi.Json

data class JsonCategory(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String
)