package com.bendezu.tinkofffintech.courses.rating_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.data.LectureEntity
import kotlinx.android.synthetic.main.item_lecture.view.*


class LecturesAdapter(private val listener: (LectureEntity)->Unit): RecyclerView.Adapter<LecturesAdapter.LectureViewHolder>() {

    var data = listOf<LectureEntity>()
        set(value) {
            val callback = LectureDiff(field, value)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lecture, parent, false)
        return LectureViewHolder(view) { listener(data[it]) }
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class LectureViewHolder(itemView: View, listener: (Int)->Unit): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { listener(adapterPosition) }
        }

        fun bind(lectureEntity: LectureEntity) {
            itemView.titleTextView.text = lectureEntity.title
        }
    }

}