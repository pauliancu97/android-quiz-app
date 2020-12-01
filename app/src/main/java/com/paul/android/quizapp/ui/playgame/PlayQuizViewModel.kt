package com.paul.android.quizapp.ui.playgame

import androidx.lifecycle.*
import com.paul.android.quizapp.models.FinishQuizInfo
import com.paul.android.quizapp.models.QuizInfo
import com.paul.android.quizapp.utils.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlayQuizViewModel: ViewModel() {

    private val stateFlow: MutableStateFlow<PlayingQuizState?> = MutableStateFlow(null)
    private val mutableFinishQuizEventLiveData: MutableLiveData<Event<FinishQuizInfo>> = MutableLiveData()
    private var questionTimer: Job? = null
    private var showResultTimer: Job? = null

    companion object {
        private const val TIME_FOR_SHOW_RESULT = 5
    }

    fun stateLiveData(): LiveData<PlayingQuizState> = stateFlow.mapNotNull { it }.asLiveData()

    fun finishQuizEventLiveData(): LiveData<Event<FinishQuizInfo>> = mutableFinishQuizEventLiveData

    init {
        viewModelScope.launch {
            stateFlow
                .mapNotNull { it }
                .distinctUntilChanged { old, new ->
                    old.timeLeftForShowingResult == new.timeLeftForShowingResult
                }
                .filter { it.timeLeftForShowingResult == 0 }
                .collect { state ->
                    showResultTimer?.cancel()
                    stateFlow.value = state.copy(
                        chosenAnswer = null,
                        currentQuestionIndex = if (state.currentQuestionIndex != state.questions.size - 1)
                            state.currentQuestionIndex + 1
                        else
                            state.currentQuestionIndex,
                        timeLeftForCurrentQuestion = state.timeLimitPerQuestion,
                        questionResult = QuestionResult.Waiting,
                        timeLeftForShowingResult = TIME_FOR_SHOW_RESULT,
                        finishedQuiz = state.currentQuestionIndex == state.questions.size - 1
                    )
                    questionTimer = viewModelScope.launch {
                        updateQuestionTimer()
                    }
                }
        }
        viewModelScope.launch {
            stateFlow
                .filterNotNull()
                .distinctUntilChanged { old, new -> old.timeLeftForCurrentQuestion == new.timeLeftForCurrentQuestion }
                .filter { it.timeLeftForCurrentQuestion == 0 }
                .collect { state ->
                    questionTimer?.cancel()
                    stateFlow.value = state.copy(
                        questionResult = QuestionResult.TimeExpired,
                        chosenAnswers = state.chosenAnswers + listOf<String?>(null)
                    )
                    showResultTimer = viewModelScope.launch {
                        updateShowResultTimer()
                    }
                }
        }
        viewModelScope.launch {
            stateFlow
                .filterNotNull()
                .filter { state -> state.finishedQuiz }
                .map { state ->
                    FinishQuizInfo(
                        quizInfo = QuizInfo(
                            questions = state.questions,
                            timeLimitPerQuestion = state.timeLimitPerQuestion
                        ),
                        score = state.score,
                        chosenAnswers = state.chosenAnswers
                    )
                }
                .collect {
                    mutableFinishQuizEventLiveData.value = Event(it)
                }
        }
    }

    private suspend fun updateQuestionTimer() {
        while(true) {
            val currentTimer = stateFlow.value?.timeLeftForCurrentQuestion ?: return
            if (currentTimer == 0) {
                break
            }
            delay(1000)
            stateFlow.value = stateFlow.value?.copy(
                timeLeftForCurrentQuestion = currentTimer - 1
            )
        }
    }

    private suspend fun updateShowResultTimer() {
        while(true) {
            val currentTimer = stateFlow.value?.timeLeftForShowingResult ?: return
            if (currentTimer == 0) {
                break
            }
            delay(1000)
            stateFlow.value = stateFlow.value?.copy(
                timeLeftForShowingResult = currentTimer - 1
            )
        }
    }

    fun initialize(quizInfo: QuizInfo) {
        stateFlow.value = PlayingQuizState(
            questions = quizInfo.questions,
            currentQuestionIndex = 0,
            timeLimitPerQuestion = quizInfo.timeLimitPerQuestion,
            timeLeftForCurrentQuestion = quizInfo.timeLimitPerQuestion,
            chosenAnswer = null,
            score = 0,
            questionResult = QuestionResult.Waiting,
            timeLeftForShowingResult = TIME_FOR_SHOW_RESULT
        )
        questionTimer = viewModelScope.launch {
            updateQuestionTimer()
        }
    }

    fun handleClicks(clickFlow: Flow<String>) {
        viewModelScope.launch {
            clickFlow.collect { answer ->
                questionTimer?.cancel()
                val currentState = stateFlow.value ?: return@collect
                if (currentState.questionResult == QuestionResult.Waiting) {
                    val currentQuestion = currentState.questions[currentState.currentQuestionIndex]
                    stateFlow.value = currentState.copy(
                        chosenAnswer = answer,
                        score = if (answer == currentQuestion.correctAnswer) currentState.score + 1 else currentState.score,
                        questionResult = if (answer == currentQuestion.correctAnswer) QuestionResult.Correct else QuestionResult.Incorrect,
                        chosenAnswers = currentState.chosenAnswers + answer
                    )
                    showResultTimer = viewModelScope.launch {
                        updateShowResultTimer()
                    }
                }
            }
        }
    }
}