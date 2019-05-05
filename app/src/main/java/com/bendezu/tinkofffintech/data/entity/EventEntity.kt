package com.bendezu.tinkofffintech.data.entity

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeString(title)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeString(customDate)
        parcel.writeString(place)
        parcel.writeString(url)
        parcel.writeByte(if (isUrlExternal) 1 else 0)
        parcel.writeByte(if (shouldDisplayButton) 1 else 0)
        parcel.writeString(urlText)
        parcel.writeString(description)
        parcel.writeString(typeName)
        parcel.writeString(typeColor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventEntity> {
        override fun createFromParcel(parcel: Parcel): EventEntity {
            return EventEntity(parcel)
        }
        override fun newArray(size: Int): Array<EventEntity?> {
            return arrayOfNulls(size)
        }
    }
}