package com.paul.android.quizapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = Question.TABLE_NAME,
    indices = [Index(value = [Question.TEXT], unique = true)]
)
data class Question(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) val id: Int? = null,
    @ColumnInfo(name = TEXT) val text: String,
    @ColumnInfo(name = DIFFICULT_ID) val difficultyId: Int,
    @ColumnInfo(name = CATEGORY_ID) val categoryId: Int,
    @ColumnInfo(name = CORRECT_ANSWER_ID) val correctAnswerId: Int
) {
    companion object {
        const val TABLE_NAME = "question"
        const val ID = "id"
        const val TEXT = "text"
        const val DIFFICULT_ID = "difficulty_id"
        const val CATEGORY_ID = "category_id"
        const val CORRECT_ANSWER_ID = "correct_answer_id"
    }
}