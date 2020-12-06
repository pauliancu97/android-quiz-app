package com.paul.android.quizapp.ui.quizhistory

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.FragmentQuizHistoryBinding
import com.paul.android.quizapp.ui.main.MainActivity
import javax.inject.Inject

class QuizHistoryFragment : Fragment() {

    private lateinit var binding: FragmentQuizHistoryBinding

    @Inject
    lateinit var quizHistoryAdapter: QuizHistoryAdapter

    @Inject
    lateinit var quizHistoryViewModelFactory: QuizHistoryViewModelFactory

    private val viewModel: QuizHistoryViewModel by viewModels{
        quizHistoryViewModelFactory.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.quizesList) {
            adapter = quizHistoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getQuizesForLoggedInUser()?.observe(viewLifecycleOwner) {
            quizHistoryAdapter.submitList(it)
        }
    }
}