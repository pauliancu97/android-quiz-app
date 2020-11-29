package com.paul.android.quizapp.ui.playgame

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.AnswerItemLayoutBinding
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

class AnswerListAdapter @Inject constructor(
   private val context: Context,
   private val resources: Resources
): ListAdapter<AnswerItem, AnswerItemViewHolder>(AnswerItemDiffUtilCallback()) {

    companion object {
        private const val ANSWER_ITEM_TYPE = 0
    }

    private val clickChannel: ConflatedBroadcastChannel<String> = ConflatedBroadcastChannel()
    val clickFlow: Flow<String> = clickChannel.asFlow()

    fun update(state: PlayingQuizState) {
        val items = with(state) {
            val currentQuestion = questions[currentQuestionIndex]
            currentQuestion.possibleAnswers.map { answer ->
                val markType = if (answer != chosenAnswer) {
                    if (questionResult == QuestionResult.Waiting) {
                        MarkType.Unmarked
                    } else {
                        if (answer == currentQuestion.correctAnswer) {
                            MarkType.MarkedAsCorrect
                        } else {
                            MarkType.Unmarked
                        }
                    }
                } else {
                    if (answer == currentQuestion.correctAnswer) {
                        MarkType.MarkedAsCorrect
                    } else {
                        MarkType.MarkedAsIncorrect
                    }
                }
                AnswerItem(
                    text = answer,
                    markedType = markType
                )
            }
        }
        submitList(items)
    }

    override fun getItemViewType(position: Int): Int = ANSWER_ITEM_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerItemViewHolder {
        val inflater = LayoutInflater.from(context)
        return AnswerItemViewHolder(
            AnswerItemLayoutBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AnswerItemViewHolder, position: Int) {
        onBindViewHolder(
            holder,
            position,
            mutableListOf(
                AnswerItemPayload(
                    hasTextChanged = true,
                    hasMarkTypeChanged = true
                )
            )
        )
    }

    override fun onBindViewHolder(
        holder: AnswerItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val payload = payloads
            .filterIsInstance<AnswerItemPayload>()
            .let {
                if (it.isEmpty()) {
                    AnswerItemPayload(true, true)
                } else {
                    AnswerItemPayload(
                        hasTextChanged = it.any { p -> p.hasTextChanged },
                        hasMarkTypeChanged = it.any { p -> p.hasMarkTypeChanged }
                    )
                }
            }
        val item = getItem(position)
        holder.button.setOnClickListener {
            clickChannel.offer(item.text)
        }
        if (payload.hasTextChanged) {
            holder.button.text = item.text
        }
        if (payload.hasMarkTypeChanged) {
            val backgroundColorId = when(item.markedType) {
                MarkType.Unmarked -> R.color.grey
                MarkType.MarkedAsCorrect -> R.color.green
                MarkType.MarkedAsIncorrect -> R.color.red
            }
            holder.button.setBackgroundColor(resources.getColor(backgroundColorId))
         }
    }

    private fun getModelItem(viewHolder: RecyclerView.ViewHolder) =
        viewHolder.adapterPosition
            .takeIf { it != RecyclerView.NO_POSITION }
            ?.let { getItem(it) }
}