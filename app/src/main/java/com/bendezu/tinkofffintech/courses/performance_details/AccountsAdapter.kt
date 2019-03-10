package com.bendezu.tinkofffintech.courses.performance_details

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.avatarColors
import kotlinx.android.synthetic.main.item_account_list.view.*

private const val LIST_ITEM_TYPE = 0
private const val GRID_ITEM_TYPE = 1

class AccountsAdapter(var layoutManager: GridLayoutManager): RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    var data = mutableListOf<String>()
        set(value) {
            val oldList = field
            val newList = value
            val callback = DiffCallback(oldList, newList)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = when (viewType) {
            LIST_ITEM_TYPE -> { LayoutInflater.from(parent.context).inflate(
                R.layout.item_account_list, parent, false) }
            GRID_ITEM_TYPE -> { LayoutInflater.from(parent.context).inflate(
                R.layout.item_account_grid, parent, false) }
            else -> throw IllegalArgumentException("unknown item ViewType")
        }
        return AccountViewHolder(view, viewType)
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return if (layoutManager.spanCount == 1) LIST_ITEM_TYPE else GRID_ITEM_TYPE
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class AccountViewHolder(itemView: View, private val viewType: Int): RecyclerView.ViewHolder(itemView) {
        fun bind(contact: String) {
            val color = avatarColors[Math.abs(contact.hashCode()) % avatarColors.size]
            val initials = contact.split(" ", limit = 2).map { it.first() }.joinToString("").toUpperCase()
            val points = Math.abs(contact.hashCode()) % 100
            itemView.avatar.setImageDrawable(ColorDrawable(color))
            itemView.avatar.initials = initials
            itemView.name.text = contact
            when(viewType) {
                LIST_ITEM_TYPE -> {itemView.points.text = itemView.context.resources.getQuantityString(
                    R.plurals.points, points, points)}
                GRID_ITEM_TYPE -> {itemView.points.text = points.toString()}
            }
        }
    }

}