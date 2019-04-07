package com.bendezu.tinkofffintech.courses.performance_details

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.bendezu.tinkofffintech.App
import com.bendezu.tinkofffintech.PREF_COOKIE
import com.bendezu.tinkofffintech.data.StudentDao
import com.bendezu.tinkofffintech.data.StudentEntity
import com.bendezu.tinkofffintech.network.GradesResponse
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import java.io.IOException
import kotlin.concurrent.thread

class StudentsRepository(private val studentDao: StudentDao,
                         private val sharedPreferences: SharedPreferences,
                         var callback: StudentsCallback? = null) {

    interface StudentsCallback {
        fun onResult(students: List<StudentEntity>, fromNetwork: Boolean = false)
        fun onError(t: Throwable)
    }

    private val uiHandler = Handler(Looper.getMainLooper())

    fun getStudents() {
        thread {
            val dbStudents = studentDao.getAll()
            uiHandler.post{ callback?.onResult(dbStudents) }

            val cookie = sharedPreferences.getString(PREF_COOKIE, "").orEmpty()
            try {
                val response = App.apiService.getGrades(cookie).execute()
                if (response.isSuccessful) {
                    val grades = response.body()?.get(1)
                    if (grades != null) {
                        val netStudents = grades.toEntity()
                        val sortedStudents = netStudents.sortedBy { it.id }

                        uiHandler.post{ callback?.onResult(sortedStudents, true) }

                        studentDao.updateData(netStudents)
                    }
                } else {
                    uiHandler.post{ callback?.onError(UnauthorizedException()) }
                }
            } catch (e: IOException) {
                uiHandler.post{ callback?.onError(NetworkException()) }
            }
        }
    }

    private fun GradesResponse.toEntity(): List<StudentEntity> {
        val students = mutableListOf<StudentEntity>()
        for (student in this.grades) {
            val total = student.grades.last().mark.toFloat()
            students.add(StudentEntity(student.id, student.name, total))
        }
        return students
    }

}