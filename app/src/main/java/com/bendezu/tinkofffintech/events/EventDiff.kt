package com.bendezu.tinkofffintech.events

import androidx.recyclerview.widget.DiffUtil
import com.bendezu.tinkofffintech.data.entity.EventEntity

class EventDiff(private val oldList: List<EventEntity>, private val newList: List<EventEntity>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title &&
                oldList[oldItemPosition].startDate == newList[newItemPosition].startDate &&
                oldList[oldItemPosition].endDate == newList[newItemPosition].endDate
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title &&
                oldList[oldItemPosition].startDate == newList[newItemPosition].startDate &&
                oldList[oldItemPosition].endDate == newList[newItemPosition].endDate &&
                oldList[oldItemPosition].typeName == newList[newItemPosition].typeName &&
                oldList[oldItemPosition].typeColor == newList[newItemPosition].typeColor
    }

}