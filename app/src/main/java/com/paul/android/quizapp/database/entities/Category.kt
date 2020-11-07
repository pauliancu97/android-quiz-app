package com.paul.android.quizapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = Category.TABLE_NAME
)
data class Category(
    @PrimaryKey @ColumnInfo(name = ID) val id: Long,
    @ColumnInfo(name = NAME) val name: String
) {
    companion object {
        const val TABLE_NAME = "category"
        const val ID = "id"
        const val NAME = "name"
    }
}