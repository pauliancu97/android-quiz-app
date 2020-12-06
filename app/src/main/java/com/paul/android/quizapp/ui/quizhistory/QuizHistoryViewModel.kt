package com.paul.android.quizapp.ui.quizhistory

import androidx.lifecycle.*
import com.paul.android.quizapp.models.QuizDescriptionModel
import com.paul.android.quizapp.repository.QuizRepository
import com.paul.android.quizapp.users.UserProvider
import javax.inject.Inject

class QuizHistoryViewModelFactory @Inject constructor(
    private val userProvider: UserProvider,
    private val quizRepository: QuizRepository
) {
    fun create() = object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return QuizHistoryViewModel(userProvider, quizRepository) as T
        }
    }
}

class QuizHistoryViewModel(
    private val userProvider: UserProvider,
    private val quizRepository: QuizRepository
): ViewModel() {

    fun getQuizesForLoggedInUser(): LiveData<List<QuizDescriptionModel>>? =
        userProvider.getUser()?.uid?.let { userUid ->
            quizRepository.getQuizesForUser(userUid).asLiveData()
        }

}