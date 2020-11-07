package com.paul.android.quizapp.ui.loadingcreatequiz

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.FragmentLoadingCreateQuizBinding
import com.paul.android.quizapp.ui.main.MainActivity
import javax.inject.Inject

class LoadingCreateQuizFragment : Fragment() {

    @Inject
    lateinit var loadingCreateQuizFragmentViewModelFactory: LoadingCreateQuizFragmentViewModelFactory

    private val viewModel: LoadingCreateQuizFragmentViewModel by viewModels {
        loadingCreateQuizFragmentViewModelFactory.create()
    }

    private lateinit var binding: FragmentLoadingCreateQuizBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoadingCreateQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.startLoading()
        viewModel.loadingFinishedLiveData().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    findNavController().navigate(R.id.action_loadingCreateQuizFragment_to_createGameFragment)
                }
            }
        }
    }
}