package com.paul.android.quizapp.ui.loading

import androidx.lifecycle.*
import com.paul.android.quizapp.models.CategoryModel
import com.paul.android.quizapp.models.DifficultyModel
import com.paul.android.quizapp.models.QuestionTypeModel
import com.paul.android.quizapp.repository.QuestionRepository
import com.paul.android.quizapp.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadingFragmentViewModelFactory @Inject constructor(
    private val questionRepository: QuestionRepository
){
    fun create() = object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoadingFragmentViewModel(questionRepository) as T
        }
    }
}

class LoadingFragmentViewModel(
    private val questionRepository: QuestionRepository
): ViewModel() {

    private lateinit var category: CategoryModel
    private lateinit var difficulty: DifficultyModel
    private lateinit var type: QuestionTypeModel
    private var questionsNumber: Int = 0
    private var timeLimit: Int = 0

    private val mutableLoadedEvent: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))

    fun getLoadedEventLiveData(): LiveData<Event<Boolean>> = mutableLoadedEvent

    fun initialize(
        category: CategoryModel,
        difficulty: DifficultyModel,
        type: QuestionTypeModel,
        questionsNumber: Int,
        timeLimit: Int
    ) {
        this.category = category
        this.difficulty = difficulty
        this.type = type
        this.questionsNumber = questionsNumber
        this.timeLimit = timeLimit
    }

    fun createQuiz() {
        viewModelScope.launch() {
            createNewQuiz()
            mutableLoadedEvent.value = Event(true)
        }
    }

    private suspend fun createNewQuiz() = withContext(Dispatchers.IO) {
        questionRepository.deleteAll()
        questionRepository.fetchQuestions(
            amount = questionsNumber,
            category = category,
            difficulty = difficulty,
            type = type
        )
    }
}