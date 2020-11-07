package com.paul.android.quizapp.ui.loadingcreatequiz

import androidx.lifecycle.*
import com.paul.android.quizapp.repository.CategoryRepository
import com.paul.android.quizapp.repository.DifficultyRepository
import com.paul.android.quizapp.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoadingCreateQuizFragmentViewModelFactory @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val difficultyRepository: DifficultyRepository
){
    fun create() = object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoadingCreateQuizFragmentViewModel(categoryRepository, difficultyRepository) as T
        }
    }
}

class LoadingCreateQuizFragmentViewModel(
    private val categoryRepository: CategoryRepository,
    private val difficultyRepository: DifficultyRepository
): ViewModel() {

    private val mutableLoadedLiveEvent: MutableLiveData<Event<Boolean>> =
        MutableLiveData(Event(false))

    fun loadingFinishedLiveData(): LiveData<Event<Boolean>> = mutableLoadedLiveEvent

    fun startLoading() {
        viewModelScope.launch {
            val categoriesAsyncJob = async(Dispatchers.IO) {
                val areCategoriesCached = categoryRepository.areCategoriesCached()
                if (!areCategoriesCached) {
                    categoryRepository.initializeCategories()
                }
            }
            val difficultiesAsyncJob = async(Dispatchers.IO) {
                val areDifficultiesCached = difficultyRepository.areDifficultiesCached()
                if (!areDifficultiesCached) {
                    difficultyRepository.insertInitialDifficulties()
                }
            }
            categoriesAsyncJob.await()
            difficultiesAsyncJob.await()
            mutableLoadedLiveEvent.value = Event(true)
        }
    }

}