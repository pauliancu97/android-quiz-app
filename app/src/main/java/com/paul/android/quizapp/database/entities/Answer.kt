package com.paul.android.quizapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = Answer.TABLE_NAME
)
data class Answer(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) val id: Long? = null,
    @ColumnInfo(name = TEXT) val text: String
) {
    companion object {
        const val TABLE_NAME = "answer"
        const val ID = "id"
        const val TEXT = "text"
    }
}