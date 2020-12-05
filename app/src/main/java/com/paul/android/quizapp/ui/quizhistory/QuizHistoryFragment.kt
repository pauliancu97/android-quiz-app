package com.paul.android.quizapp.ui.quizhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.FragmentQuizHistoryBinding

class QuizHistoryFragment : Fragment() {

    private lateinit var binding: FragmentQuizHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }
}