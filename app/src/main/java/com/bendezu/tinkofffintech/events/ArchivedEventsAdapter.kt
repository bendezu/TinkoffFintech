package com.bendezu.tinkofffintech.events

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.data.entity.EventEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.parseColor
import kotlinx.android.synthetic.main.item_event_list.view.*
import javax.inject.Inject

@ActivityScope
class ArchivedEventsAdapter @Inject constructor(): RecyclerView.Adapter<ArchivedEventsAdapter.ArchivedEventsViewHolder>() {

    var listener: (EventEntity)->Unit = {}

    var data = listOf<EventEntity>()
        set(value) {
            val callback = EventDiff(field, value)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchivedEventsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_list, parent, false)
        return ArchivedEventsViewHolder(view) { listener(data[it]) }
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ArchivedEventsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class ArchivedEventsViewHolder(itemView: View, listener: (Int)->Unit): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { listener(adapterPosition) }
        }

        fun bind(event: EventEntity) {
            itemView.titleTextView.text = event.title
            val color = event.typeColor.parseColor(itemView.context)
            itemView.typeImageView.setImageDrawable(ColorDrawable(color))
            if (event.typeName != null && event.typeColor != null) {
                itemView.typeNameTextView.text = event.typeName
            } else {
                itemView.typeNameTextView.visibility = View.GONE
            }
        }
    }

}