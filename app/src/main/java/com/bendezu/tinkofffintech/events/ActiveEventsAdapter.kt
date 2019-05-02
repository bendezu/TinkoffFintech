package com.bendezu.tinkofffintech.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.data.entity.EventEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.parseColor
import kotlinx.android.synthetic.main.item_event_card.view.*
import kotlinx.android.synthetic.main.item_lecture.view.titleTextView
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import java.text.SimpleDateFormat
import java.util.*
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
                val color = event.typeColor.parseColor(itemView.context)
                DrawableCompat.setTint(itemView.typeNameTextView.background, color)
            } else {
                itemView.typeNameTextView.visibility = View.GONE
            }
            itemView.dateTextView.text = formatPeriod(event.startDate, event.endDate)
        }

        private fun formatPeriod(startDateStr: String, endDateStr: String): String {
            val startDate = Instant.parse(startDateStr).atZone(ZoneId.systemDefault()).toLocalDate()
            val endDate = Instant.parse(endDateStr).atZone(ZoneId.systemDefault()).toLocalDate()
            val monthFormat = SimpleDateFormat("LLL", Locale.getDefault())
            val cal = Calendar.getInstance()
            cal.set(startDate.year, startDate.monthValue - 1, startDate.dayOfMonth)
            val startMonth = monthFormat.format(cal.time).trimEnd('.')
            cal.set(endDate.year, endDate.monthValue - 1, endDate.dayOfMonth)
            val endMonth = monthFormat.format(cal.time).trimEnd('.')
            var period = (if (startMonth == endMonth) startMonth else "$startMonth - $endMonth")
            period += " ${cal.get(Calendar.YEAR)}"
            return period
        }
    }

}