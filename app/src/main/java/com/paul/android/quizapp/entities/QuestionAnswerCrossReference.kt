package com.paul.android.quizapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = QuestionAnswerCrossReference.TABLE_NAME,
    primaryKeys = [
        QuestionAnswerCrossReference.QUESTION_ID,
        QuestionAnswerCrossReference.ANSWER_ID
    ],
    foreignKeys = [
        ForeignKey(
            entity = Question::class,
            parentColumns = [Question.ID],
            childColumns = [QuestionAnswerCrossReference.QUESTION_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Answer::class,
            parentColumns = [Answer.ID],
            childColumns = [QuestionAnswerCrossReference.ANSWER_ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QuestionAnswerCrossReference(
    @ColumnInfo(name = QUESTION_ID) val questionId: Long,
    @ColumnInfo(name = ANSWER_ID) val answerId: Long
) {
    companion object {
        const val TABLE_NAME = "question_answer"
        const val QUESTION_ID = "question_id"
        const val ANSWER_ID = "answer_id"
    }
}