package com.bendezu.tinkofffintech.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student")
data class StudentEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "total_mark") val totalMark: Float
)