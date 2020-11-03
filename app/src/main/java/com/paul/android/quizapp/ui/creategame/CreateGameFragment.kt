package com.paul.android.quizapp.ui.creategame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.FragmentCraeteGameBinding

class CreateGameFragment : Fragment() {

    private lateinit var binding: FragmentCraeteGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCraeteGameBinding.inflate(inflater, container, false)
        return binding.root
    }
}