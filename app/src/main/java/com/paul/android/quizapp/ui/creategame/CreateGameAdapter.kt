package com.paul.android.quizapp.ui.creategame

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.CreateQuizMessageLayoutBinding
import com.paul.android.quizapp.databinding.InputNumLayoutBinding
import com.paul.android.quizapp.databinding.InputSelectLayoutBinding
import com.paul.android.quizapp.models.CategoryModel
import com.paul.android.quizapp.models.CreateQuizStateModel
import com.paul.android.quizapp.models.DifficultyModel
import com.paul.android.quizapp.models.QuestionTypeModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.lang.IllegalStateException
import javax.inject.Inject

enum class SelectInputIdentifier(val stringId: Int) {
    Category(R.string.category_label),
    Difficulty(R.string.difficulty_label),
    Type(R.string.type_label)
}

enum class NumberInputIdentifier(val stringId: Int) {
    QuestionsNumber(R.string.questions_number_label),
    TimeLimit(R.string.time_limit_label)
}

sealed class Click {
    data class SelectClick(val selectInputIdentifier: SelectInputIdentifier): Click()
    data class MinusClick(val numberInputIdentifier: NumberInputIdentifier): Click()
    data class PlusClick(val numberInputIdentifier: NumberInputIdentifier): Click()
}

class CreateGameAdapter @Inject constructor(
    private val context: Context,
    private val resources: Resources
): ListAdapter<CreateGameAdapter.Item, CreateGameAdapter.ItemHolder>(DiffUtilCallback()) {

    sealed class Item {
        object Message: Item()
        data class SelectInput(
            internal val identifier: SelectInputIdentifier,
            val selection: String
        ): Item() {
            fun compare(other: SelectInput): Int =
                (if (identifier != other.identifier) PAYLOAD_SELECT_NAME else 0) or
                        (if (selection != other.selection) PAYLOAD_SELECT_SELECTION else 0)
        }
        data class NumberInput(
            internal val identifier: NumberInputIdentifier,
            val value: Int,
            val isMinusButtonEnabled: Boolean = true,
            val isPlusButtonEnabled: Boolean = true
        ): Item() {
            fun compare(other: NumberInput): Int =
                (if (identifier != other.identifier) PAYLOAD_NUM_NAME else 0) or
                        (if (value != other.value) PAYLOAD_NUM_VALUE else 0) or
                        (if (isMinusButtonEnabled != other.isMinusButtonEnabled) PAYLOAD_NUM_MINUS_BTN_ENABLED else 0) or
                        (if (isPlusButtonEnabled != other.isPlusButtonEnabled) PAYLOAD_NUM_PLUS_BTN_ENABLED else 0)
        }
    }

    sealed class ItemHolder(view: View): RecyclerView.ViewHolder(view) {
        class MessageHolder(binding: CreateQuizMessageLayoutBinding): ItemHolder(binding.root)
        class SelectInputHolder(binding: InputSelectLayoutBinding): ItemHolder(binding.root) {
            val label = binding.inputLabel
            val button = binding.inputButton
        }
        class NumberInputHolder(binding: InputNumLayoutBinding): ItemHolder(binding.root) {
            val label = binding.inputLabel
            val edit = binding.inputEdit
            val minusButton = binding.minusButton
            val plusButton = binding.plusButton
        }
    }

    class DiffUtilCallback: DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
            (oldItem is Item.Message && newItem is Item.Message) ||
                    (oldItem is Item.SelectInput && newItem is Item.SelectInput && oldItem.identifier == newItem.identifier) ||
                    (oldItem is Item.NumberInput && newItem is Item.NumberInput && oldItem.identifier == newItem.identifier)

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
            if(oldItem is Item.Message && newItem is Item.Message) {
                true
            } else {
                oldItem == newItem
            }

        override fun getChangePayload(oldItem: Item, newItem: Item) =
            if (oldItem is Item.Message && newItem is Item.Message) {
                PAYLOAD_ALL
            } else if (oldItem is Item.SelectInput && newItem is Item.SelectInput) {
                oldItem.compare(newItem)
            } else if(oldItem is Item.NumberInput && newItem is Item.NumberInput) {
                oldItem.compare(newItem)
            } else {
                PAYLOAD_ALL
            }
    }

    companion object {
        private const val MESSAGE_TYPE = 0
        private const val SELECT_INPUT_TYPE = 1
        private const val NUMBER_INPUT_TYPE = 2

        private const val PAYLOAD_SELECT_NAME = 1
        private const val PAYLOAD_SELECT_SELECTION = 1 shl 1

        private const val PAYLOAD_NUM_NAME = 1
        private const val PAYLOAD_NUM_VALUE = 1 shl 1
        private const val PAYLOAD_NUM_MINUS_BTN_ENABLED = 1 shl 2
        private const val PAYLOAD_NUM_PLUS_BTN_ENABLED = 1 shl 3

        private const val PAYLOAD_ALL = -1
    }

    @ExperimentalCoroutinesApi
    private val clickChannel: ConflatedBroadcastChannel<Click> = ConflatedBroadcastChannel()
    val clickFlow: Flow<Click> = clickChannel.asFlow()

    private fun CategoryModel.toUIRepresentation() =
        when(this) {
            CategoryModel.AnyCategoryModel -> resources.getString(R.string.any_label)
            is CategoryModel.ConcreteCategoryModel -> this.name
        }

    private fun DifficultyModel.toUIRepresentation() =
        resources.getString(this.stringId)

    private fun QuestionTypeModel.toUiRepresentation() =
        resources.getString(this.stringId)

    fun update(state: CreateQuizStateModel) {
        val categoryItem = Item.SelectInput(
            SelectInputIdentifier.Category,
            state.category.toUIRepresentation()
        )
        val difficultyItem = Item.SelectInput(
            SelectInputIdentifier.Difficulty,
            state.difficulty.toUIRepresentation()
        )
        val typeItem = Item.SelectInput(
            SelectInputIdentifier.Type,
            state.type.toUiRepresentation()
        )
        val questionsNumberItem = Item.NumberInput(
            NumberInputIdentifier.QuestionsNumber,
            state.questionsNumber,
            state.questionsNumber > state.minNumQuestions,
            state.questionsNumber < state.maxNumQuestions
        )
        val timeLimitItem = Item.NumberInput(
            NumberInputIdentifier.TimeLimit,
            state.timeLimit,
            state.timeLimit > state.minTimeLimit,
            state.timeLimit < state.maxTimeLimit
        )
        val items = listOf(categoryItem, difficultyItem, typeItem, questionsNumberItem, timeLimitItem)
        submitList(items)
    }

    override fun getItemViewType(position: Int): Int =
        when(getItem(position)) {
            Item.Message -> MESSAGE_TYPE
            is Item.SelectInput -> SELECT_INPUT_TYPE
            is Item.NumberInput -> NUMBER_INPUT_TYPE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(context)
        return when(viewType) {
            MESSAGE_TYPE -> ItemHolder.MessageHolder(CreateQuizMessageLayoutBinding.inflate(inflater, parent, false))
            SELECT_INPUT_TYPE -> ItemHolder.SelectInputHolder(InputSelectLayoutBinding.inflate(inflater, parent, false)).also { viewHolder ->
                viewHolder.button.setOnClickListener {
                    getModelItem<Item.SelectInput>(viewHolder)?.let {
                        clickChannel.offer(Click.SelectClick(it.identifier))
                    }
                }
            }
            NUMBER_INPUT_TYPE -> ItemHolder.NumberInputHolder(InputNumLayoutBinding.inflate(inflater, parent, false)).also { viewHolder ->
                viewHolder.minusButton.setOnClickListener {
                    getModelItem<Item.NumberInput>(viewHolder)?.let {
                        clickChannel.offer(Click.MinusClick(it.identifier))
                    }
                }
                viewHolder.plusButton.setOnClickListener {
                    getModelItem<Item.NumberInput>(viewHolder)?.let {
                        clickChannel.offer(Click.PlusClick(it.identifier))
                    }
                }
            }
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        onBindViewHolder(holder, position, emptyList<Int>())
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int, payloads: List<Any>) {
        val payload = payloads
            .filterIsInstance<Int>()
            .fold(initial = PAYLOAD_ALL) { a, b -> a or b }
        val item = getItem(position)
        when {
            item is Item.SelectInput && holder is ItemHolder.SelectInputHolder -> bind(item, holder, payload)
            item is Item.NumberInput && holder is ItemHolder.NumberInputHolder -> bind(item, holder, payload)
        }
    }

    private fun bind(item: Item.SelectInput, holder: ItemHolder.SelectInputHolder, payload: Int) {
        if (0 != payload.and(PAYLOAD_SELECT_NAME)) {
            holder.label.text = resources.getString(item.identifier.stringId)
        }
        if (0 != payload.and(PAYLOAD_SELECT_SELECTION)) {
            holder.button.text = item.selection
        }
    }

    private fun bind(item: Item.NumberInput, holder:ItemHolder.NumberInputHolder, payload: Int) {
        if (0 != payload.and(PAYLOAD_NUM_NAME)) {
            holder.label.text = resources.getString(item.identifier.stringId)
        }
        if (0 != payload.and(PAYLOAD_NUM_NAME)) {
            holder.edit.setText(item.value.toString())
        }
        if (0 != payload.and(PAYLOAD_NUM_MINUS_BTN_ENABLED)) {
            holder.minusButton.isEnabled = item.isMinusButtonEnabled
        }
        if (0 != payload.and(PAYLOAD_NUM_PLUS_BTN_ENABLED)) {
            holder.plusButton.isEnabled = item.isPlusButtonEnabled
        }
    }

    private inline fun <reified T: Item> getModelItem(viewHolder: RecyclerView.ViewHolder) =
        viewHolder.adapterPosition
            .takeIf { it != RecyclerView.NO_POSITION }
            ?.let {  getItem(it) as? T }
}