package com.paul.android.quizapp.ui.finishgame

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.FragmentFinishQuizBinding
import com.paul.android.quizapp.ui.main.MainActivity
import com.paul.android.quizapp.ui.playgame.PlayQuizFragment
import java.lang.IllegalStateException
import javax.inject.Inject

class FinishQuizFragment : Fragment() {

    companion object {
        const val KEY_FINISH_QUIZ_INFO = "FINISH_QUIZ_INFO"
    }

    @Inject
    lateinit var viewModelFactory: FinishQuizViewModelFactory
    private lateinit var binding: FragmentFinishQuizBinding
    private val viewModel: FinishQuizViewModel by viewModels {
        viewModelFactory.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_finishQuizFragment_to_startFragment)
        }.apply { isEnabled = true }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinishQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.finishQuizInfo == null) {
            viewModel.finishQuizInfo = arguments?.getParcelable(KEY_FINISH_QUIZ_INFO) ?: throw IllegalStateException()
            viewModel.addInRealtimeDatabase()
        }
        viewModel.finishQuizInfo?.apply {
            binding.yourScoreLabel.text = getString(
                R.string.score_label,
                score,
                quizInfo.questions.size
            )
        }
        binding.createNewQuizButton.setOnClickListener {
            findNavController().navigate(R.id.action_finishQuizFragment_to_createGameFragment)
        }
        viewModel.finishQuizInfo?.quizInfo?.let { quizInfo ->
            val shuffledQuizInfo = with(quizInfo) {
                copy(
                    questions = questions
                        .map { question ->
                            question.copy(
                                possibleAnswers = question.possibleAnswers.shuffled()
                            )
                        }
                        .shuffled()
                )
            }
            binding.playAgainButton.setOnClickListener {
                findNavController().navigate(
                    R.id.action_finishQuizFragment_to_playQuizFragment,
                    bundleOf(PlayQuizFragment.KEY_QUIZ_INFO to shuffledQuizInfo)
                )
            }
        }
    }
}