package com.paul.android.quizapp.ui.creategame

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.SelectOptionDialogLayoutBinding
import com.paul.android.quizapp.ui.main.MainActivity
import javax.inject.Inject

class SelectOptionDialogFragment: DialogFragment() {

    @Inject
    lateinit var optionsAdapter: SelectOptionsAdapter

    @Inject
    lateinit var selectOptionsViewModelFactory: SelectOptionsViewModelFactory

    private val viewModel: SelectOptionsViewModel by activityViewModels()

    private lateinit var binding: SelectOptionDialogLayoutBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SelectOptionDialogLayoutBinding.inflate(inflater, container, false)
        with(binding.optionsList) {
            adapter = optionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.doneButton.setOnClickListener {
            viewModel.handleDone()
            dismiss()
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    companion object {
        fun newInstance() = SelectOptionDialogFragment()
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleOptionsClicks(optionsAdapter.clickFlow)
        viewModel.getAdapterStateLiveData().observe(viewLifecycleOwner) {
            with (binding.optionTitle) {
                text = when (it.identifier) {
                    SelectInputIdentifier.Category -> getString(R.string.choose_category_label)
                    SelectInputIdentifier.Difficulty -> getString(R.string.choose_difficulty_label)
                    SelectInputIdentifier.Type -> getString(R.string.choose_type_label)
                }
            }
            optionsAdapter.update(it)
            val selectionIndex = it.options.indexOf(it.selectedOption)
            if(selectionIndex != -1) {
                binding.optionsList.post {
                    binding.optionsList.scrollToPosition(selectionIndex)
                }
            }
        }
    }

}