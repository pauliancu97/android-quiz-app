package com.paul.android.quizapp.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        binding.playQuizButton.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_createGameFragment)
        }
        return binding.root
    }
}