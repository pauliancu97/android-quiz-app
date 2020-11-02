package com.paul.android.quizapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = QuestionAnswerCrossReference.TABLE_NAME,
    primaryKeys = [
        QuestionAnswerCrossReference.QUESTION_ID,
        QuestionAnswerCrossReference.ANSWER_ID
    ]
)
data class QuestionAnswerCrossReference(
    @ColumnInfo(name = QUESTION_ID) val questionId: Int,
    @ColumnInfo(name = ANSWER_ID) val answerId: Int
) {
    companion object {
        const val TABLE_NAME = "question_answer"
        const val QUESTION_ID = "question_id"
        const val ANSWER_ID = "answer_id"
    }
}