package com.paul.android.quizapp.ui.creategame

import android.content.res.Resources
import androidx.lifecycle.*
import com.paul.android.quizapp.R
import com.paul.android.quizapp.models.CategoryModel
import com.paul.android.quizapp.models.DifficultyModel
import com.paul.android.quizapp.models.QuestionTypeModel
import com.paul.android.quizapp.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectOptionsViewModelFactory @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val resources: Resources
) {
    fun create() = object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SelectOptionsViewModel(categoryRepository, resources) as T
        }
    }
}

class SelectOptionsViewModel(
    private val categoryRepository: CategoryRepository,
    private val resources: Resources
): ViewModel() {

    data class SelectionState(
        val options: List<Pair<String, Any>>,
        val selection: Pair<String, Any>,
        val identifier: SelectInputIdentifier
    )

    private val stateFlow: MutableStateFlow<SelectionState?> = MutableStateFlow(null)
    private val adapterStateFlow: MutableStateFlow<SelectOptionsAdapterState?> = MutableStateFlow(null)

    private val categoryChannel: ConflatedBroadcastChannel<CategoryModel> = ConflatedBroadcastChannel()
    private val difficultyChannel: ConflatedBroadcastChannel<DifficultyModel> = ConflatedBroadcastChannel()
    private val typeChannel: ConflatedBroadcastChannel<QuestionTypeModel> = ConflatedBroadcastChannel()

    fun getCategoryLiveData(): LiveData<CategoryModel> = categoryChannel.asFlow().asLiveData()
    fun getDifficultyLiveData(): LiveData<DifficultyModel> = difficultyChannel.asFlow().asLiveData()
    fun getTypeLiveData(): LiveData<QuestionTypeModel> = typeChannel.asFlow().asLiveData()
    fun getAdapterStateLiveData(): LiveData<SelectOptionsAdapterState> =
        adapterStateFlow.filterNotNull().asLiveData()

    fun handleDone() {
        val state = stateFlow.value
        if (state != null) {
            val selection = state.selection.second
            when (state.identifier) {
                SelectInputIdentifier.Category -> categoryChannel.offer(selection as CategoryModel)
                SelectInputIdentifier.Difficulty -> difficultyChannel.offer(selection as DifficultyModel)
                SelectInputIdentifier.Type -> typeChannel.offer(selection as QuestionTypeModel)
            }
        }
    }

    init {
        viewModelScope.launch {
            stateFlow
                .filterNotNull()
                .collect { state ->
                    val options = state.options.map { it.first }
                    val selection = state.selection.first
                    adapterStateFlow.value = SelectOptionsAdapterState(options, selection)
                }
        }
    }

    fun handleOptionsClicks(clickFlow: Flow<Int>) {
        viewModelScope.launch {
            clickFlow.collect { selectionIndex ->
                val currentState = stateFlow.value
                if(currentState != null) {
                    val selection = currentState.options[selectionIndex]
                    stateFlow.value = currentState.copy(selection = selection)
                }
            }
        }
    }

    fun initialize(identifier: SelectInputIdentifier) {
        when (identifier) {
            SelectInputIdentifier.Category -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val categories = categoryRepository.getAllCategories()
                    val options = categories.map {
                        val name = when (it) {
                            CategoryModel.AnyCategoryModel -> resources.getString(R.string.any_label)
                            is CategoryModel.ConcreteCategoryModel -> it.name
                        }
                        Pair(name, it)
                    }
                    val selection = options.firstOrNull()
                    if (selection != null) {
                        stateFlow.value = SelectionState(options, selection, identifier)
                    }
                }
            }
            SelectInputIdentifier.Type -> {
                val options = QuestionTypeModel.values()
                    .map {
                        val name = resources.getString(it.stringId)
                        Pair(name, it)
                    }
                val selection = options.firstOrNull()
                if (selection != null) {
                    stateFlow.value = SelectionState(options, selection, identifier)
                }
            }
            SelectInputIdentifier.Difficulty -> {
                val options = DifficultyModel.values()
                    .map {
                        val name = resources.getString(it.stringId)
                        Pair(name, it)
                    }
                val selection = options.firstOrNull()
                if (selection != null) {
                    stateFlow.value = SelectionState(options, selection, identifier)
                }
            }
        }
    }
}