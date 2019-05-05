package com.bendezu.tinkofffintech.courses.performance_details

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.data.dao.StudentDao
import com.bendezu.tinkofffintech.data.entity.StudentEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.getCookie
import com.bendezu.tinkofffintech.getRecentStudentUpdate
import com.bendezu.tinkofffintech.getUser
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.models.User
import com.bendezu.tinkofffintech.network.models.toEntity
import com.bendezu.tinkofffintech.saveRecentStudentUpdate
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.Flowables
import javax.inject.Inject

private const val VALID_DURATION_MILLIS = 10_000

@ActivityScope
class StudentsRepository @Inject constructor(private val studentDao: StudentDao,
                                             private val sharedPreferences: SharedPreferences,
                                             private val apiService: FintechApiService) {

    fun getStudents(): Flowable<Pair<User, List<StudentEntity>>> {

        val cookie = sharedPreferences.getCookie()
        val prevUpdate = sharedPreferences.getRecentStudentUpdate()
        val isDataValid = System.currentTimeMillis() - prevUpdate < VALID_DURATION_MILLIS
        var studentsSource = studentDao.getAllRx().toFlowable()
        val networkResponse = apiService.getConnectionsRx(cookie)
            .flatMap { apiService.getGradesRx(cookie, it.courses[0].url)}
            .flatMap { response ->
                val grades = response[1]
                val students = grades.toEntity()
                val sortedStudents = students.sortedBy { it.id }
                sharedPreferences.saveRecentStudentUpdate(System.currentTimeMillis())
                studentDao.updateData(students)
                return@flatMap Single.fromCallable { sortedStudents }
            }
        if (!isDataValid) studentsSource = studentsSource.concatWith(networkResponse)

        return Flowables.combineLatest(Flowable.fromCallable { sharedPreferences.getUser() }, studentsSource)
    }
}