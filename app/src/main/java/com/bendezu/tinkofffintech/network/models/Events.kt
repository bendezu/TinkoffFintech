package com.bendezu.tinkofffintech.network.models

import com.bendezu.tinkofffintech.data.entity.EventEntity
import com.google.gson.annotations.SerializedName

class EventsResponse (
    @SerializedName("active") val activeEvents: List<Event>,
    @SerializedName("archive") val archivedEvents: List<Event>
)

class Event (
    @SerializedName("title") val title: String,
    @SerializedName("date_start") val startDate: String,
    @SerializedName("date_end") val endDate: String,
    @SerializedName("event_type") val eventType: EventType?,
    @SerializedName("custom_date") val customDate: String,
    @SerializedName("place") val place: String?,
    @SerializedName("url") val url: String,
    @SerializedName("url_external") val isUrlExternal: Boolean,
    @SerializedName("display_button") val shouldDisplayButton: Boolean,
    @SerializedName("url_text") val urlText: String,
    @SerializedName("description") val description: String
)

class EventType (
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: String
)

fun Event.toEntity(isActive: Boolean) = EventEntity (
    isActive, title, startDate, endDate, customDate, place,
    url, isUrlExternal, shouldDisplayButton, urlText, description, eventType?.name, eventType?.color
)