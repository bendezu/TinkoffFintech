package com.bendezu.tinkofffintech.courses.performance_details

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.data.StudentEntity
import com.bendezu.tinkofffintech.getAvatarColor
import com.bendezu.tinkofffintech.getInitials
import kotlinx.android.synthetic.main.item_account_list.view.*
import kotlin.math.floor

class AccountsAdapter: RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    var filteredData = listOf<StudentEntity>()
        set(value) {
            val callback = StudentDiff(field, value)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }

    private var data = listOf<StudentEntity>()

    fun setNewData(value: List<StudentEntity>, query: String) {
        data = value
        filter(query)
    }

    fun filter(query: String) {
        val pattern = query.toLowerCase().trim()
        val filtered = data.filter { it.name.toLowerCase().contains(pattern) }
        filteredData = filtered
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_account_list, parent, false)
        return AccountViewHolder(view)
    }

    override fun getItemCount() = filteredData.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(filteredData[position])
    }

    class AccountViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(student: StudentEntity) {
            val points = student.totalMark
            itemView.avatar.setImageDrawable(ColorDrawable(student.name.getAvatarColor()))
            itemView.avatar.initials = student.name.getInitials()
            itemView.name.text = student.name
            itemView.points.text = itemView.context.resources.getQuantityString(
                    R.plurals.points, floor(points).toInt(), points)
        }
    }

}