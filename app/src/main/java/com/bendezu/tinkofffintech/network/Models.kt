package com.bendezu.tinkofffintech.network

import com.google.gson.annotations.SerializedName

class User (
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstname: String,
    @SerializedName("last_name") val lastname: String,
    @SerializedName("middle_name") val middlename: String? = null,
    @SerializedName("avatar") val avatar: String? = null
)

class UserCredential (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

class UserResponse (
    @SerializedName("user") val user: User,
    @SerializedName("status") val status: String
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

class GradesResponse (
    @SerializedName("grades") val grades: List<Student>
)

class Student (
    @SerializedName("student_id") val id: Long,
    @SerializedName("student") val name: String,
    @SerializedName("grades") val grades: List<Grade>
)

class Grade (
    @SerializedName("mark") val mark: String
)