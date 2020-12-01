package com.paul.android.quizapp.ui.loading

import androidx.lifecycle.*
import com.paul.android.quizapp.models.CategoryModel
import com.paul.android.quizapp.models.DifficultyModel
import com.paul.android.quizapp.models.QuestionTypeModel
import com.paul.android.quizapp.models.QuizInfo
import com.paul.android.quizapp.repository.QuestionRepository
import com.paul.android.quizapp.users.UserProvider
import com.paul.android.quizapp.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadingFragmentViewModelFactory @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val userProvider: UserProvider
){
    fun create() = object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoadingFragmentViewModel(
                questionRepository,
                userProvider
            ) as T
        }
    }
}

class LoadingFragmentViewModel(
    private val questionRepository: QuestionRepository,
    private val userProvider: UserProvider
): ViewModel() {

    private lateinit var category: CategoryModel
    private lateinit var difficulty: DifficultyModel
    private lateinit var type: QuestionTypeModel
    private var questionsNumber: Int = 0
    private var timeLimit: Int = 0

    private val mutableLoadedEvent: MutableLiveData<Event<QuizInfo>> = MutableLiveData()

    fun getLoadedEventLiveData(): LiveData<Event<QuizInfo>> = mutableLoadedEvent

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
        viewModelScope.launch {
            val questions = createNewQuiz()
            if (userProvider.getUser() != null) {
                questionRepository.addToRealtimeDatabase(questions)
            }
            mutableLoadedEvent.value = Event(
                QuizInfo(
                    questions = questions,
                    timeLimitPerQuestion = timeLimit
                )
            )
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
        questionRepository.getAll()
    }
}