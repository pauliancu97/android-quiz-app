package com.paul.android.quizapp.models

sealed class CategoryModel {
    object AnyCategoryModel: CategoryModel()
    data class ConcreteCategoryModel(
        val id: Long,
        val name: String
    ): CategoryModel()

    fun toParameter() = (this as? ConcreteCategoryModel)?.id
}