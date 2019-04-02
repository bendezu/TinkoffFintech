package com.bendezu.tinkofffintech.data

import androidx.room.*

@Entity(tableName = "lecture")
data class LectureEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "title") val title: String
)

@Entity(tableName = "task", foreignKeys = [
    ForeignKey(
        entity = LectureEntity::class,
        parentColumns = ["id"],
        childColumns = ["lecture_id"],
        onDelete = ForeignKey.CASCADE)
], indices = [Index("lecture_id")])
data class TaskEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "task_type") val taskType: String,
    @ColumnInfo(name = "max_score") val maxScore: String,
    @ColumnInfo(name = "deadline_date") val deadlineDate: String?,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "mark") val mark: String,
    @ColumnInfo(name = "lecture_id") val lectureId: Long
)