package com.bendezu.tinkofffintech.courses

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.getAvatarColor
import com.bendezu.tinkofffintech.getInitials
import com.bendezu.tinkofffintech.view.AccountBadgeView
import kotlin.math.roundToInt

class StudentBadgesAdapter : RecyclerView.Adapter<StudentBadgesAdapter.StudentBadgeViewHolder>() {

    var data = listOf<StudentBadge>()
        set(value) {
            val callback = StudentBadgeDiff(field, value)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentBadgeViewHolder {
        val itemView = AccountBadgeView(parent.context)
        itemView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return StudentBadgeViewHolder(itemView)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: StudentBadgeViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class StudentBadgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(student: StudentBadge) {
            val view = itemView as AccountBadgeView
            view.initials = student.name.getInitials()
            view.avatarBackground = ColorDrawable(student.name.getAvatarColor())
            view.points = student.points.roundToInt().toString()
            if (student.points == 0F) view.visibility = View.INVISIBLE
            view.highlighted = student.highlighted

            val name = student.name.split(" ").getOrNull(1) ?: student.name
            view.name = if (student.highlighted) view.context.getString(R.string.you) else name
        }
    }

}