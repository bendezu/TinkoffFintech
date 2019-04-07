package com.bendezu.tinkofffintech.courses.performance_details

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.getAvatarColor
import com.bendezu.tinkofffintech.getInitials
import kotlinx.android.synthetic.main.item_account_list.view.*

class AccountsAdapter: RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    var filteredData = mutableListOf<String>()
        set(value) {
            val callback = DiffCallback(field, value)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }
    var data = mutableListOf<String>()
        set(value) {
            field = value
            filteredData = data
        }

    val filter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val pattern = (constraint ?: "").toString().toLowerCase().trim()
            val filtered = data.filter { it.toLowerCase().contains(pattern) }
            return FilterResults().apply { values = filtered }
        }
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredData = results?.values as MutableList<String>
        }
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

        fun bind(contact: String) {
            val points = Math.abs(contact.hashCode()) % 100
            itemView.avatar.setImageDrawable(ColorDrawable(contact.getAvatarColor()))
            itemView.avatar.initials = contact.getInitials()
            itemView.name.text = contact
            itemView.points.text = itemView.context.resources.getQuantityString(
                    R.plurals.points, points, points)
        }
    }

}