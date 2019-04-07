package com.bendezu.tinkofffintech.courses.performance_details

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.getAvatarColor
import com.bendezu.tinkofffintech.getInitials
import kotlinx.android.synthetic.main.item_account_list.view.*

private const val LIST_ITEM_TYPE = 0
private const val GRID_ITEM_TYPE = 1

class AccountsAdapter(var layoutManager: GridLayoutManager): RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    var data = mutableListOf<String>()
        set(value) {
            val callback = DiffCallback(field, value)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = when (viewType) {
            LIST_ITEM_TYPE -> {
                LayoutInflater.from(parent.context).inflate(R.layout.item_account_list, parent, false)
            }
            GRID_ITEM_TYPE -> {
                LayoutInflater.from(parent.context).inflate(R.layout.item_account_grid, parent, false)
            }
            else -> throw IllegalArgumentException("unknown item ViewType")
        }
        return AccountViewHolder(view, viewType)
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) = if (layoutManager.spanCount == COLUMNS_LIST) LIST_ITEM_TYPE else GRID_ITEM_TYPE

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class AccountViewHolder(itemView: View, private val viewType: Int): RecyclerView.ViewHolder(itemView) {

        fun bind(contact: String) {
            val points = Math.abs(contact.hashCode()) % 100
            itemView.avatar.setImageDrawable(ColorDrawable(contact.getAvatarColor()))
            itemView.avatar.initials = contact.getInitials()
            itemView.name.text = contact
            when(viewType) {
                LIST_ITEM_TYPE -> {itemView.points.text = itemView.context.resources.getQuantityString(
                    R.plurals.points, points, points)}
                GRID_ITEM_TYPE -> {itemView.points.text = points.toString()}
            }
        }
    }

}