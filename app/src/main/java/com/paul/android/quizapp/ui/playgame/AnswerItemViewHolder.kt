package com.paul.android.quizapp.ui.playgame

import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.paul.android.quizapp.databinding.AnswerItemLayoutBinding

class AnswerItemViewHolder(binding: AnswerItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
        val button: Button = binding.answerButton
}