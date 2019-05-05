package com.bendezu.tinkofffintech.events

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.data.entity.EventEntity
import com.bendezu.tinkofffintech.network.FintechApiService.Companion.BASE_URL_CONCAT
import com.bendezu.tinkofffintech.parseColor
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_event_detail.*
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class EventDetailFragment : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_EVENT = "event"
        fun newInstance(event: EventEntity): EventDetailFragment {
            return EventDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_EVENT, event)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = arguments?.getParcelable<EventEntity>(ARG_EVENT)
        if (event != null) showEvent(event)
    }

    private fun showEvent(event: EventEntity) {
        titleTextView.text = event.title

        if (event.customDate.isNotEmpty())
            dateTextView.text = event.customDate
        else
            dateTextView.text = parseDates(event.startDate, event.endDate)

        val color = event.typeColor.parseColor(requireContext())
        typeTextView.text = event.typeName ?: requireContext().getString(R.string.event)
        DrawableCompat.setTint(typeTextView.background, color)

        placeTextView.text = event.place
        descriptionTextView.text = event.description
        linkTextView.text = event.urlText
        linkTextView.setOnClickListener {
            val url = if (event.isUrlExternal) event.url else BASE_URL_CONCAT + event.url
            openUrl(url)
        }
    }

    private fun openUrl(url: String) {
        val uri = Uri.parse(url)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun parseDates(startDateStr: String, endDateStr: String): String {
        val startDate = Instant.parse(startDateStr).atZone(ZoneId.systemDefault()).toLocalDate()
        val endDate = Instant.parse(endDateStr).atZone(ZoneId.systemDefault()).toLocalDate()
        val date = if (startDate == endDate) {
            startDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        } else {
            val formatter = DateTimeFormatter.ofPattern("dd MMM")
            "${startDate.format(formatter)} - ${endDate.format(formatter)}"
        }
        return date
    }
}