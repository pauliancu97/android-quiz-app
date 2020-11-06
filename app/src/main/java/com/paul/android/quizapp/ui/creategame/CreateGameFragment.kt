package com.paul.android.quizapp.ui.creategame

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.FragmentCraeteGameBinding
import com.paul.android.quizapp.ui.QuizApplication
import com.paul.android.quizapp.ui.main.MainActivity
import javax.inject.Inject

class CreateGameFragment : Fragment() {

    private lateinit var binding: FragmentCraeteGameBinding

    @Inject
    lateinit var createGameAdapter: CreateGameAdapter

    @Inject
    lateinit var selectOptionsViewModelFactory: SelectOptionsViewModelFactory

    private val viewModel: CreateGameFragmentViewModel by viewModels()

    private val selectOptionsViewModel: SelectOptionsViewModel by activityViewModels {
        selectOptionsViewModelFactory.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCraeteGameBinding.inflate(inflater, container, false)
        with(binding.list) {
            adapter = createGameAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleClicks(createGameAdapter.clickFlow) {
            selectOptionsViewModel.initialize(
                identifier = it,
                category = viewModel.getSelectedCategory(),
                difficulty = viewModel.getSelectedDifficulty(),
                type = viewModel.getSelectedType()
            )
            showSelectOptionsDialog()
        }
        viewModel.getState().observe(viewLifecycleOwner) {
            createGameAdapter.update(it)
        }
        selectOptionsViewModel.getCategoryLiveData().observe(viewLifecycleOwner) {
            viewModel.setCategory(it)
        }
        selectOptionsViewModel.getDifficultyLiveData().observe(viewLifecycleOwner) {
            viewModel.setDifficulty(it)
        }
        selectOptionsViewModel.getTypeLiveData().observe(viewLifecycleOwner) {
            viewModel.setType(it)
        }
    }

    private fun showSelectOptionsDialog() {
        val dialog = SelectOptionDialogFragment.newInstance()
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        val previousDialog = parentFragmentManager.findFragmentByTag(SELECT_DIALOG)
        if (previousDialog != null) {
            fragmentTransaction.remove(previousDialog)
        }
        fragmentTransaction.addToBackStack(null)
        dialog.show(fragmentTransaction, SELECT_DIALOG)
    }

    companion object {
        private const val SELECT_DIALOG = "SELECT_DIALOG"
    }
}