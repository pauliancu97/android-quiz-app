package com.paul.android.quizapp.ui.playgame

import androidx.recyclerview.widget.DiffUtil

data class AnswerItemPayload(
    val hasTextChanged: Boolean = false,
    val hasMarkTypeChanged: Boolean = false
)

data class AnswerItem(
    val text: String,
    val markedType: MarkType = MarkType.Unmarked
) {
    fun compare(other: AnswerItem) =
        AnswerItemPayload(
            hasTextChanged = text != other.text,
            hasMarkTypeChanged = markedType != other.markedType
        )
}

enum class MarkType {
    Unmarked,
    MarkedAsCorrect,
    MarkedAsIncorrect
}

class AnswerItemDiffUtilCallback: DiffUtil.ItemCallback<AnswerItem>() {

    override fun areItemsTheSame(oldItem: AnswerItem, newItem: AnswerItem): Boolean =
        oldItem.text == newItem.text

    override fun areContentsTheSame(oldItem: AnswerItem, newItem: AnswerItem): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: AnswerItem, newItem: AnswerItem) =
        oldItem.compare(newItem)
}