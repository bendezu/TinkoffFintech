package com.bendezu.tinkofffintech.network.models

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