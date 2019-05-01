package com.bendezu.tinkofffintech.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lecture")
data class LectureEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "title") val title: String
)