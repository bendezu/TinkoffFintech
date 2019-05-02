package com.bendezu.tinkofffintech.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.data.entity.EventEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import kotlinx.android.synthetic.main.item_event_card.view.*
import kotlinx.android.synthetic.main.item_lecture.view.titleTextView
import javax.inject.Inject

@ActivityScope
class ActiveEventsAdapter @Inject constructor(): RecyclerView.Adapter<ActiveEventsAdapter.ActiveEventsViewHolder>() {

    var listener: (EventEntity)->Unit = {}

    var data = listOf<EventEntity>()
        set(value) {
            val callback = EventDiff(field, value)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveEventsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_card, parent, false)
        return ActiveEventsViewHolder(view) { listener(data[it]) }
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ActiveEventsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class ActiveEventsViewHolder(itemView: View, listener: (Int)->Unit): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { listener(adapterPosition) }
        }

        fun bind(event: EventEntity) {
            itemView.titleTextView.text = event.title
            if (event.typeName != null && event.typeColor != null) {
                itemView.typeNameTextView.text = event.typeName
            } else {
                itemView.typeNameTextView.visibility = View.GONE
            }
        }
    }

}