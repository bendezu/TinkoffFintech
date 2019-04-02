package com.bendezu.tinkofffintech.courses.rating_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.data.TaskEntity
import com.bendezu.tinkofffintech.data.TaskStatus
import kotlinx.android.synthetic.main.item_task.view.*
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class TasksAdapter: RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    var data = listOf<TaskEntity>()
        set(value) {
            val callback = TaskDiff(field, value)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(task: TaskEntity) {
            val (statusRes, colorRes) = when (task.status) {
                TaskStatus.NEW -> {
                    R.string.noviy to R.color.colorPrimaryLight
                }
                TaskStatus.ACCEPTED -> {
                    R.string.passed to R.color.colorPassed
                }
                TaskStatus.ON_CHECK -> {
                    R.string.checking to R.color.colorAccentDark
                }
                else -> {
                    R.string.failed to R.color.colorFailed
                }
            }
            itemView.apply {
                val color = ContextCompat.getColor(context, colorRes)
                titleTextView.text = task.title
                statusTextView.setText(statusRes)
                statusTextView.setTextColor(color)
                markTextView.text = context.getString(R.string.mark, task.mark, task.maxScore)
                DrawableCompat.setTint(markTextView.background, color)
                if (task.deadlineDate != null) {
                    val date = formatDate(task.deadlineDate)
                    deadlineTextView.text = context.getString(R.string.deadline, date)
                }
            }
        }

        private fun formatDate(dateStr: String): String {
            val instant = Instant.parse(dateStr)
            val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
            return localDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm"))
        }
    }

}