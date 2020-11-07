package com.paul.android.quizapp.ui.start

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.FragmentStartBinding
import com.paul.android.quizapp.ui.main.MainActivity
import javax.inject.Inject

class StartFragment : Fragment() {

    @Inject
    lateinit var startFragmentViewModelFactory: StartFragmentViewModelFactory

    private val viewModel: StartFragmentViewModel by viewModels {
        startFragmentViewModelFactory.create()
    }

    private lateinit var binding: FragmentStartBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        binding.playQuizButton.setOnClickListener {
            viewModel.handleLoading()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.isLoadingRequiredLiveData().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { isLoadingRequired ->
                if (isLoadingRequired) {
                    findNavController().navigate(R.id.action_startFragment_to_loadingCreateQuizFragment)
                } else {
                    findNavController().navigate(R.id.action_startFragment_to_createGameFragment)
                }
            }
        }
    }
}