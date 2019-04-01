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
    val homeworks: List<HomeWork>
)

class HomeWork (
    val id: Long,
    val title: String,
    val tasks: List<Task>
)

class Task (
    val id: Long,
    @SerializedName("task") val taskData: TaskData,
    val status: String,
    val mark: String
)

class TaskData (
    val id: Long,
    val title: String,
    @SerializedName("task_type") val taskType: String,
    @SerializedName("max_score") val maxScore: String,
    @SerializedName("deadline_date") val deadlineDate: String?
)