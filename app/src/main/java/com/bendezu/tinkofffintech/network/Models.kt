package com.bendezu.tinkofffintech.network

import com.google.gson.annotations.SerializedName

class User (
    val email: String,

    @SerializedName("first_name")
    val firstname: String,

    @SerializedName("last_name")
    val lastname: String,

    @SerializedName("middle_name")
    val middlename: String? = null,

    val avatar: String? = null
)

class UserCredential (
    val email: String,
    val password: String
)

class UserResponse (
    val user: User,
    val status: String
)

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