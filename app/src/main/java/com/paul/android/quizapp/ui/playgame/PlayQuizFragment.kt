package com.paul.android.quizapp.ui.playgame

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.FragmentPlayQuizBinding
import com.paul.android.quizapp.models.QuizInfo
import com.paul.android.quizapp.ui.finishgame.FinishQuizFragment
import com.paul.android.quizapp.ui.main.MainActivity
import java.lang.IllegalStateException
import javax.inject.Inject

class PlayQuizFragment : Fragment() {

    companion object {
        const val KEY_QUIZ_INFO = "KEY_QUIZ_INFO"
    }

    private lateinit var binding: FragmentPlayQuizBinding
    private val viewModel: PlayQuizViewModel by viewModels()

    @Inject
    lateinit var answerListAdapter: AnswerListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayQuizBinding.inflate(inflater, container, false)
        with(binding.answersList) {
            adapter = answerListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val quizInfo = arguments?.getParcelable<QuizInfo>(KEY_QUIZ_INFO) ?: throw IllegalStateException()
        viewModel.initialize(quizInfo)
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleClicks(answerListAdapter.clickFlow)
        viewModel.stateLiveData().observe(viewLifecycleOwner) {
            val currentQuestion = it.questions[it.currentQuestionIndex]
            binding.difficultyLabel.text = currentQuestion.difficulty
            binding.categoryLabel.text = currentQuestion.category
            binding.timeLeftIndicatorText.text = it.timeLeftForCurrentQuestion.toString()
            binding.timeLeftIndicatorBar.max = it.timeLimitPerQuestion
            binding.timeLeftIndicatorBar.progress = it.timeLeftForCurrentQuestion
            binding.questionText.text = currentQuestion.questionText
            answerListAdapter.update(it)
        }
        viewModel.finishQuizEventLiveData().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { finishQuizInfo ->
                findNavController().navigate(
                    R.id.action_playQuizFragment_to_finishQuizFragment,
                    bundleOf(FinishQuizFragment.KEY_FINISH_QUIZ_INFO to finishQuizInfo)
                )
            }
        }
    }
}