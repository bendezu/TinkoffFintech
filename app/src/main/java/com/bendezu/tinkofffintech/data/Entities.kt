package com.bendezu.tinkofffintech.data

import androidx.room.*
import java.util.*

@Entity(tableName = "lecture")
data class LectureEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "task_id") val taskId: Long
)

@Entity(tableName = "task", foreignKeys = [
    ForeignKey(entity = LectureEntity::class, parentColumns = ["id"], childColumns = ["lecture_id"])
], indices = [Index("lecture_id")])
data class TaskEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "task_type") val taskType: String,
    @ColumnInfo(name = "max_score") val maxScore: Double,
    @ColumnInfo(name = "deadline_date") val deadlineDate: Date,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "mark") val mark: String,
    @ColumnInfo(name = "lecture_id") val lectureId: Long
)