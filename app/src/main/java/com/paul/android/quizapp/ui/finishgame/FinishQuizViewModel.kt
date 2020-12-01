package com.paul.android.quizapp.ui.finishgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paul.android.quizapp.models.FinishQuizInfo
import com.paul.android.quizapp.repository.QuizRepository
import com.paul.android.quizapp.users.UserProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FinishQuizViewModelFactory @Inject constructor(
    private val quizRepository: QuizRepository,
    private val userProvider: UserProvider
){
    fun create() = object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FinishQuizViewModel(quizRepository, userProvider) as T
        }
    }
}

class FinishQuizViewModel(
    private val quizRepository: QuizRepository,
    private val userProvider: UserProvider
): ViewModel() {
    var finishQuizInfo: FinishQuizInfo? = null

    fun addInRealtimeDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val quizInfo = finishQuizInfo
            if (userProvider.getUser() != null && quizInfo != null) {
                quizRepository.addQuiz(quizInfo)
            }
        }
    }
}