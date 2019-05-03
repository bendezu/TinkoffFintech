package com.bendezu.tinkofffintech.courses

import androidx.recyclerview.widget.DiffUtil

class StudentBadgeDiff(private val oldList: List<StudentBadge>, private val newList: List<StudentBadge>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val first = oldList[oldItemPosition]
        val second = newList[newItemPosition]
        return  first.name == second.name &&
                first.points == second.points &&
                first.highlighted == first.highlighted
    }

}