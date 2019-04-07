package com.bendezu.tinkofffintech.courses.rating_details

import androidx.recyclerview.widget.DiffUtil
import com.bendezu.tinkofffintech.data.LectureEntity

class LectureDiff(private val oldList: List<LectureEntity>, private val newList: List<LectureEntity>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}