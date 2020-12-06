package com.paul.android.quizapp.ui.quizhistory

import androidx.recyclerview.widget.DiffUtil
import com.paul.android.quizapp.models.QuizDescriptionModel

class QuizDescriptionModelDiffUtilCallback: DiffUtil.ItemCallback<QuizDescriptionModel>() {
    override fun areContentsTheSame(
        oldItem: QuizDescriptionModel,
        newItem: QuizDescriptionModel
    ) = oldItem == newItem

    override fun areItemsTheSame(
        oldItem: QuizDescriptionModel,
        newItem: QuizDescriptionModel
    ) = oldItem.date == newItem.date

    override fun getChangePayload(
        oldItem: QuizDescriptionModel,
        newItem: QuizDescriptionModel
    ) = QuizDescriptionModelPayload(
        hasDateChanged = oldItem.date != newItem.date,
        hasScoreChanged = (oldItem.score != newItem.score) || (oldItem.numOfQuestions != newItem.numOfQuestions),
        hasTimeLimitChanged = oldItem.timeLimit != newItem.timeLimit
    )
}