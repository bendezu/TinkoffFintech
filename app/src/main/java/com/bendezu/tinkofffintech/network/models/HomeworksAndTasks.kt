package com.bendezu.tinkofffintech.network.models

import com.bendezu.tinkofffintech.data.entity.LectureEntity
import com.bendezu.tinkofffintech.data.entity.TaskEntity
import com.google.gson.annotations.SerializedName

class HomeworksResponse (
    @SerializedName("homeworks") val homeworks: List<HomeWork>
)

class HomeWork (
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("tasks") val tasks: List<Task>
)

class Task (
    @SerializedName("id") val id: Long,
    @SerializedName("task") val taskData: TaskData,
    @SerializedName("status") val status: String,
    @SerializedName("mark") val mark: String
)

class TaskData (
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("task_type") val taskType: String,
    @SerializedName("max_score") val maxScore: String,
    @SerializedName("deadline_date") val deadlineDate: String?
)

fun HomeworksResponse.toEntities(): Pair<List<LectureEntity>, List<TaskEntity>> {
    val homeworks = this.homeworks
    val lectures = mutableListOf<LectureEntity>()
    val tasks = mutableListOf<TaskEntity>()
    for (homework in homeworks) {
        lectures.add(LectureEntity(homework.id, homework.title))
        for (task in homework.tasks) {
            val taskData = task.taskData
            tasks.add(
                TaskEntity(
                    taskData.id,
                    taskData.title,
                    taskData.taskType,
                    taskData.maxScore,
                    taskData.deadlineDate,
                    task.status,
                    task.mark,
                    homework.id
                )
            )
        }
    }
    return lectures to tasks
}