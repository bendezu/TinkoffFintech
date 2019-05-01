package com.bendezu.tinkofffintech.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "event", primaryKeys = ["title", "date_start", "date_end"])
data class EventEntity(
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "date_start") val startDate: String,
    @ColumnInfo(name = "date_end") val endDate: String,
    @ColumnInfo(name = "custom_date") val customDate: String,
    @ColumnInfo(name = "place") val place: String?,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "url_external") val isUrlExternal: Boolean,
    @ColumnInfo(name = "display_button") val shouldDisplayButton: Boolean,
    @ColumnInfo(name = "url_text") val urlText: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "type_name") val typeName: String? = null,
    @ColumnInfo(name = "type_color") val typeColor: String? = null
)