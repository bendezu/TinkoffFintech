package com.bendezu.tinkofffintech.courses.performance_details

import androidx.recyclerview.widget.DiffUtil
import com.bendezu.tinkofffintech.data.StudentEntity

class StudentDiff(private val oldList: List<StudentEntity>, private val newList: List<StudentEntity>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}