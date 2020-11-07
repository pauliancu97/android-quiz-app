package com.paul.android.quizapp.ui.loading

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.FragmentLoadingBinding
import com.paul.android.quizapp.models.*
import com.paul.android.quizapp.ui.main.MainActivity
import java.lang.IllegalStateException
import javax.inject.Inject

class LoadingFragment : Fragment() {

    companion object {
        const val CATEGORY_ARG = "CATEGORY_ARG"
        const val DIFFICULTY_ARG = "DIFFICULTY_ARG"
        const val TYPE_ARG = "TYPE_ARG"
        const val QUESTIONS_NUM_ARG = "QUESTIONS_NUM_ARG"
        const val TIME_LIMIT_ARG = "TIME_LIMIT_ARG"
    }

    private lateinit var binding: FragmentLoadingBinding

    @Inject
    lateinit var loadingFragmentViewModelFactory: LoadingFragmentViewModelFactory

    private val viewModel: LoadingFragmentViewModel by viewModels {
        loadingFragmentViewModelFactory.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = arguments?.getParcelable<ParcelableCategoryModel>(CATEGORY_ARG)?.toModel() ?: throw IllegalStateException()
        val difficulty = arguments?.getParcelable<DifficultyModel>(DIFFICULTY_ARG) ?: throw IllegalStateException()
        val type = arguments?.getParcelable<QuestionTypeModel>(TYPE_ARG) ?: throw IllegalStateException()
        val questionsNumber = arguments?.getInt(QUESTIONS_NUM_ARG) ?: throw IllegalStateException()
        val timeLimit = arguments?.getInt(TIME_LIMIT_ARG) ?: throw IllegalStateException()
        viewModel.initialize(category, difficulty, type, questionsNumber, timeLimit)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.createQuiz()
        viewModel.getLoadedEventLiveData().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { isLoaded ->
                if (isLoaded) {
                    findNavController().navigate(R.id.action_loadingFragment_to_gameFragment)
                }
            }
        }
    }
}