package com.paul.android.quizapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paul.android.quizapp.repository.CategoryRepository
import com.paul.android.quizapp.repository.DifficultyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModelFactory @Inject constructor(
    private val difficultyRepository: DifficultyRepository,
    private val categoryRepository: CategoryRepository
){
    fun create() = object: ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainActivityViewModel(
                difficultyRepository,
                categoryRepository
            ) as T
        }
    }
}

class MainActivityViewModel(
    private val difficultyRepository: DifficultyRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {
    fun initialize() {
        viewModelScope.launch(Dispatchers.IO) {
            difficultyRepository.insertInitialDifficulties()
            categoryRepository.initializeCategories()
        }
    }
}