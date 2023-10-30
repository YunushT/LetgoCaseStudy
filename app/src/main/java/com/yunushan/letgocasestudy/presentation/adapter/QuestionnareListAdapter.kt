package com.yunushan.letgocasestudy.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yunushan.letgocasestudy.data.model.uimodel.QuestionnareListItem
import com.yunushan.letgocasestudy.databinding.SelectionItemBinding

class QuestionnareListAdapter(private val onItemClick:(selectedItem: String) -> Unit) :
    ListAdapter<String, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return QuestionnareItemViewHolder(
            SelectionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as QuestionnareItemViewHolder).bind(getItem(position))
    }

    class QuestionnareItemViewHolder(private val binding: SelectionItemBinding, val onItemClick: (selectedItem: String) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            with(binding) {
                itemText.text = text
                binding.itemText.setOnClickListener {
                    onItemClick.invoke(text)
                }
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean = oldItem == newItem
        }
    }
}