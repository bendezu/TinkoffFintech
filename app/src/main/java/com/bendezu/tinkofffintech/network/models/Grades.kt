package com.bendezu.tinkofffintech.network.models

import com.bendezu.tinkofffintech.data.entity.StudentEntity
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

fun GradesResponse.toEntity(): List<StudentEntity> {
    val students = mutableListOf<StudentEntity>()
    for (student in this.grades) {
        val total = student.grades.last().mark.toFloat()
        students.add(StudentEntity(student.id, student.name, total))
    }
    return students
}