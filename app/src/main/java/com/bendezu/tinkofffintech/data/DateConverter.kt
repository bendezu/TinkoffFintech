package com.bendezu.tinkofffintech.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS000'Z'"

class DateConverter {

    @TypeConverter
    fun toDate(dateStr: String) = SimpleDateFormat(DATE_PATTERN).parse(dateStr)

    @TypeConverter
    fun fromDate(date: Date) = SimpleDateFormat(DATE_PATTERN).format(date)
}