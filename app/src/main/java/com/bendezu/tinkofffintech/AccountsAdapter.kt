package com.bendezu.fintech.basiccomponents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R
import kotlinx.android.synthetic.main.item_account.view.*

class AccountsAdapter: RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    var data = mutableListOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class AccountViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(contact: String) {
            itemView.name.text = contact
            val points = Math.abs(contact.hashCode() % 100)
            itemView.points.text = itemView.context.resources.getQuantityString(R.plurals.points, points, points)
        }
    }

}