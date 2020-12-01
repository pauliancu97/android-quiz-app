package com.paul.android.quizapp.ui.start

import androidx.lifecycle.*
import com.paul.android.quizapp.repository.CategoryRepository
import com.paul.android.quizapp.repository.DifficultyRepository
import com.paul.android.quizapp.users.UserProvider
import com.paul.android.quizapp.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StartFragmentViewModelFactory @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val difficultyRepository: DifficultyRepository,
    private val userProvider: UserProvider
) {
    fun create() = object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return StartFragmentViewModel(
                categoryRepository,
                difficultyRepository,
                userProvider
            ) as T
        }
    }
}

class StartFragmentViewModel(
    private val categoryRepository: CategoryRepository,
    private val difficultyRepository: DifficultyRepository,
    private val userProvider: UserProvider
): ViewModel() {

    private val mutableIsLoadingRequiredLiveEvent: MutableLiveData<Event<Boolean>> =
        MutableLiveData()

    fun isLoadingRequiredLiveData(): LiveData<Event<Boolean>> = mutableIsLoadingRequiredLiveEvent

    fun handleLoading() {
        viewModelScope.launch {
            val areCategoriesCached = withContext(Dispatchers.IO) {categoryRepository.areCategoriesCached()  }
            val areDifficultiesCached = withContext(Dispatchers.IO) { difficultyRepository.areDifficultiesCached() }
            val isLoadingRequired = !areCategoriesCached || !areDifficultiesCached
            mutableIsLoadingRequiredLiveEvent.value = Event(isLoadingRequired)
        }
    }

    fun userLiveData() = userProvider.userFlow.asLiveData()

    fun updateUser() {
        userProvider.update()
    }
}