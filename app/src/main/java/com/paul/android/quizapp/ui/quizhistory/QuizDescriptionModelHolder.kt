package com.paul.android.quizapp.ui.quizhistory

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.paul.android.quizapp.databinding.QuizHistoryItemLayoutBinding

class QuizDescriptionModelHolder(binding: QuizHistoryItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
    val dateTextView: TextView = binding.dateValue
    val scoreTextView: TextView = binding.scoreValue
    val timeLimitTextView: TextView = binding.timeLimitValue
}