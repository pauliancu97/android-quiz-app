package com.paul.android.quizapp.ui.creategame

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.paul.android.quizapp.models.CategoryModel
import com.paul.android.quizapp.models.CreateQuizStateModel
import com.paul.android.quizapp.models.DifficultyModel
import com.paul.android.quizapp.models.QuestionTypeModel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CreateGameFragmentViewModel: ViewModel() {

    private val stateChannel: ConflatedBroadcastChannel<CreateQuizStateModel> = ConflatedBroadcastChannel(
        CreateQuizStateModel(
            category = CategoryModel.AnyCategoryModel,
            difficulty = DifficultyModel.AnyType,
            type = QuestionTypeModel.AnyType,
            questionsNumber = 10,
            timeLimit = 10,
            maxNumQuestions = CreateQuizStateModel.MAX_QUESTIONS_NUMBER,
            minNumQuestions = CreateQuizStateModel.MIN_QUESTIONS_NUMBER,
            maxTimeLimit = CreateQuizStateModel.MAX_TIME_LIMIT,
            minTimeLimit = CreateQuizStateModel.MIN_TIME_LIMIT
        )
    )


    fun getStateLiveData(): LiveData<CreateQuizStateModel> = stateChannel.asFlow().asLiveData()

    fun setCategory(category: CategoryModel) {
        stateChannel.offer(stateChannel.value.copy(category = category))
    }

    fun setDifficulty(difficulty: DifficultyModel) {
        stateChannel.offer(stateChannel.value.copy(difficulty = difficulty))
    }

    fun setType(type: QuestionTypeModel) {
        stateChannel.offer(stateChannel.value.copy(type = type))
    }

    fun getSelectedCategory() = stateChannel.valueOrNull?.category

    fun getSelectedDifficulty() = stateChannel.valueOrNull?.difficulty

    fun getSelectedType() = stateChannel.valueOrNull?.type

    fun getState() = stateChannel.value

    fun handleClicks(clicksFlow: Flow<Click?>, selectCallback: (SelectInputIdentifier) -> Unit) {
        viewModelScope.launch {
            clicksFlow
                .filterNotNull()
                .collect {
                    when (it) {
                        is Click.MinusClick -> {
                            val updatedState = when (it.numberInputIdentifier) {
                                NumberInputIdentifier.QuestionsNumber -> stateChannel.value.copy(
                                    questionsNumber = if (stateChannel.value.questionsNumber > stateChannel.value.minNumQuestions) stateChannel.value.questionsNumber - 1 else stateChannel.value.questionsNumber
                                )
                                NumberInputIdentifier.TimeLimit -> stateChannel.value.copy(
                                    timeLimit = if (stateChannel.value.timeLimit > stateChannel.value.minTimeLimit) stateChannel.value.timeLimit - 1 else stateChannel.value.timeLimit
                                )
                            }
                            stateChannel.offer(updatedState)
                        }
                        is Click.PlusClick -> {
                            val updatedState = when (it.numberInputIdentifier) {
                                NumberInputIdentifier.QuestionsNumber -> stateChannel.value.copy(
                                    questionsNumber = if (stateChannel.value.questionsNumber < stateChannel.value.maxNumQuestions) stateChannel.value.questionsNumber + 1 else stateChannel.value.questionsNumber
                                )
                                NumberInputIdentifier.TimeLimit -> stateChannel.value.copy(
                                    timeLimit = if (stateChannel.value.timeLimit < stateChannel.value.maxTimeLimit) stateChannel.value.timeLimit + 1 else stateChannel.value.timeLimit
                                )
                            }
                            stateChannel.offer(updatedState)
                        }
                        is Click.SelectClick -> {
                            selectCallback(it.selectInputIdentifier)
                        }
                    }
                }
        }
    }
}