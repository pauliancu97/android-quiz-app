package com.paul.android.quizapp.database.entities

import androidx.room.*

@Entity(
    tableName = Question.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Difficulty::class,
            parentColumns = [Difficulty.ID],
            childColumns = [Question.DIFFICULTY_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = [Category.ID],
            childColumns = [Question.CATEGORY_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Answer::class,
            parentColumns = [Answer.ID],
            childColumns = [Question.CORRECT_ANSWER_ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Question(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) val id: Long? = null,
    @ColumnInfo(name = TEXT) val text: String,
    @ColumnInfo(name = DIFFICULTY_ID) val difficultyId: Long,
    @ColumnInfo(name = CATEGORY_ID) val categoryId: Long,
    @ColumnInfo(name = CORRECT_ANSWER_ID) val correctAnswerId: Long
) {
    companion object {
        const val TABLE_NAME = "question"
        const val ID = "id"
        const val TEXT = "text"
        const val DIFFICULTY_ID = "difficulty_id"
        const val CATEGORY_ID = "category_id"
        const val CORRECT_ANSWER_ID = "correct_answer_id"
    }
}