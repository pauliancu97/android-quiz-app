package com.paul.android.quizapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class CategoryModel {
    object AnyCategoryModel: CategoryModel()
    data class ConcreteCategoryModel(
        val id: Long,
        val name: String
    ): CategoryModel()

    fun toParameter() = (this as? ConcreteCategoryModel)?.id
}

@Parcelize
data class ParcelableCategoryModel(
    val isConcrete: Boolean,
    val id: Long,
    val name: String
): Parcelable {
    companion object {

        private const val ANY_CATEGORY_NAME = "Any"
        private const val ANY_CATEGORY_ID = -1L

        val ANY_CATEGORY = ParcelableCategoryModel(
            isConcrete = false,
            name = ANY_CATEGORY_NAME,
            id = ANY_CATEGORY_ID
        )
    }
}

fun CategoryModel.toParcelable() =
    when (this) {
        CategoryModel.AnyCategoryModel -> ParcelableCategoryModel.ANY_CATEGORY
        is CategoryModel.ConcreteCategoryModel -> ParcelableCategoryModel(
            isConcrete = true,
            name = this.name,
            id = this.id
        )
    }

fun ParcelableCategoryModel.toModel() =
    if (isConcrete) {
        CategoryModel.ConcreteCategoryModel(
            name = name,
            id = id
        )
    } else {
        CategoryModel.AnyCategoryModel
    }