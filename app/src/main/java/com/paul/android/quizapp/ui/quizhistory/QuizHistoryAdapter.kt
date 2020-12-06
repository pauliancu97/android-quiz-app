package com.paul.android.quizapp.ui.quizhistory

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.QuizHistoryItemLayoutBinding
import com.paul.android.quizapp.models.QuizDescriptionModel
import java.text.SimpleDateFormat
import javax.inject.Inject

class QuizHistoryAdapter @Inject constructor(
    private val context: Context,
    private val resources: Resources
): ListAdapter<QuizDescriptionModel, QuizDescriptionModelHolder>(QuizDescriptionModelDiffUtilCallback()) {

    companion object {
        private const val ITEM_TYPE = 1
        private const val DATE_FORMAT = "dd/MM/yyyy HH:mm"
    }

    override fun getItemViewType(position: Int) = ITEM_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizDescriptionModelHolder {
        val inflater = LayoutInflater.from(context)
        return QuizDescriptionModelHolder(QuizHistoryItemLayoutBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: QuizDescriptionModelHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onBindViewHolder(
        holder: QuizDescriptionModelHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val payload = payloads
            .filterIsInstance<QuizDescriptionModelPayload>()
            .takeIf { it.isNotEmpty() }
            ?.let { quizPayloads ->
                QuizDescriptionModelPayload(
                    hasDateChanged = quizPayloads.any { it.hasDateChanged },
                    hasScoreChanged = quizPayloads.any { it.hasScoreChanged },
                    hasTimeLimitChanged = quizPayloads.any { it.hasTimeLimitChanged }
                )
            } ?: QuizDescriptionModelPayload()
        val item = getItem(position)
        if (payload.hasDateChanged) {
            val formatter = SimpleDateFormat(DATE_FORMAT)
            holder.dateTextView.text = formatter.format(item.date)
        }
        if (payload.hasScoreChanged) {
            holder.scoreTextView.text = resources.getString(R.string.score_label, item.score, item.numOfQuestions)
        }
        if (payload.hasTimeLimitChanged) {
            holder.timeLimitTextView.text = resources.getString(R.string.time_limit_value, item.timeLimit)
        }
    }
}