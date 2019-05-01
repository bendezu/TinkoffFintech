package com.bendezu.tinkofffintech.network.models

import com.google.gson.annotations.SerializedName

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