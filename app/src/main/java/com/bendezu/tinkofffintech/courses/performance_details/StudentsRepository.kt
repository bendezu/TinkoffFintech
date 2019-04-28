package com.bendezu.tinkofffintech.courses.performance_details

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.data.StudentDao
import com.bendezu.tinkofffintech.data.StudentEntity
import com.bendezu.tinkofffintech.getCookie
import com.bendezu.tinkofffintech.getRecentStudentUpdate
import com.bendezu.tinkofffintech.getUser
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.GradesResponse
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.bendezu.tinkofffintech.saveRecentStudentUpdate
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

private const val VALID_DURATION_MILLIS = 10_000

class StudentsRepository(private val studentDao: StudentDao,
                         private val sharedPreferences: SharedPreferences,
                         private val apiService: FintechApiService,
                         var callback: StudentsCallback? = null) {

    interface StudentsCallback {
        fun onResult(students: List<StudentEntity>, shouldStopLoading: Boolean = false)
        fun onError(t: Throwable)
    }

    private val disposables = CompositeDisposable()

    fun getStudents() {

        val cookie = sharedPreferences.getCookie()
        val prevUpdate = sharedPreferences.getRecentStudentUpdate()
        var isDataValid = System.currentTimeMillis() - prevUpdate < VALID_DURATION_MILLIS
        var studentsSource = studentDao.getAllRx().toFlowable()
        val networkResponse = apiService.getGradesRx(cookie).flatMap { response ->
            val grades = response[1]
            val netStudents = grades.toEntity()
            val sortedStudents = netStudents.sortedBy { it.id }
            sharedPreferences.saveRecentStudentUpdate(System.currentTimeMillis())
            studentDao.updateData(netStudents)
            return@flatMap Single.fromCallable { sortedStudents }
        }
        if (!isDataValid) studentsSource = studentsSource.concatWith(networkResponse)

        disposables += Flowables.combineLatest(Flowable.fromCallable { sharedPreferences.getUser() }, studentsSource)
            .map { pair ->
                val (user, students) = pair
                for (student in students) {
                    if (student.id == user.id)
                        student.name += " (Вы)"
                }
                return@map students
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ students ->
                callback?.onResult(students, isDataValid)
                isDataValid = true
            }, { t ->
                when (t) {
                    is HttpException -> if (t.code() == 403) callback?.onError(UnauthorizedException())
                    else -> callback?.onError(NetworkException())
                }
            })
    }

    fun dispose() {
        disposables.dispose()
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