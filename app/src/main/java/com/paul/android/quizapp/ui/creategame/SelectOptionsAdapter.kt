package com.paul.android.quizapp.ui.creategame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paul.android.quizapp.databinding.OptionItemLayoutBinding
import kotlinx.android.synthetic.main.option_item_layout.view.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

data class SelectOptionsAdapterState(
    val options: List<String>,
    val selectedOption: String
)

class SelectOptionsAdapter @Inject constructor(
   private val context: Context
): ListAdapter<SelectOptionsAdapter.Item, SelectOptionsAdapter.ItemHolder>(DiffUtilCallback()) {

    data class Item(
        val name: String,
        val isSelected: Boolean
    ) {
        fun compare(other: Item) = Payload(
            nameChanged = name != other.name,
            isSelectedChanged = isSelected != other.isSelected
        )
    }

    class ItemHolder(binding: OptionItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        val view: View = binding.root
        val optionName: TextView = view.optionName
        val optionButton: RadioButton = view.optionButton
    }

    data class Payload(
        val nameChanged: Boolean = true,
        val isSelectedChanged: Boolean = true
    )

    class DiffUtilCallback: DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Item, newItem: Item) =
            oldItem == newItem

        override fun getChangePayload(oldItem: Item, newItem: Item) =
            oldItem.compare(newItem)
    }

    companion object {
        private const val ITEM_TYPE = 1
    }

    private val clickChannel: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel()
    val clickFlow: Flow<Int> =  clickChannel.asFlow()

    fun update(state: SelectOptionsAdapterState) {
        val items = state.options.map { Item(name = it, isSelected = it == state.selectedOption) }
        submitList(items)
    }

    override fun getItemViewType(position: Int) = ITEM_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(context)
        return ItemHolder(OptionItemLayoutBinding.inflate(inflater, parent, false))
            .also {
                    viewHolder -> viewHolder.view.setOnClickListener {
                        clickChannel.offer(viewHolder.adapterPosition)
                    }
            }
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = getItem(position)
        bind(item, holder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int, payloads: List<Any>) {
        val payload = payloads
            .filterIsInstance<Payload>()
            .let { actualPayloads ->
                if (actualPayloads.isEmpty()) {
                    Payload(true, true)
                } else {
                    Payload(
                        nameChanged = actualPayloads.any { it.nameChanged },
                        isSelectedChanged = actualPayloads.any { it.isSelectedChanged }
                    )
                }
            }
        val item = getItem(position)
        bind(item, holder, payload)
    }

    private fun bind(item: Item, holder: ItemHolder, payload: Payload) {
        if (payload.nameChanged) {
            holder.optionName.text = item.name
        }
        if (payload.isSelectedChanged) {
            holder.optionButton.isChecked = item.isSelected
        }
    }

    private fun bind(item: Item, holder: ItemHolder) {
        holder.optionName.text = item.name
        holder.optionButton.isChecked = item.isSelected
    }
}