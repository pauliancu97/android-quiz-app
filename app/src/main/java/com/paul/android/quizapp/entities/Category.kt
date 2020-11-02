package com.paul.android.quizapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = Category.TABLE_NAME,
    indices = [Index(value = [Category.NAME], unique = true)]
)
data class Category(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) val id: Int? = null,
    @ColumnInfo(name = NAME) val name: String
) {
    companion object {
        const val TABLE_NAME = "category"
        const val ID = "id"
        const val NAME = "name"
    }
}