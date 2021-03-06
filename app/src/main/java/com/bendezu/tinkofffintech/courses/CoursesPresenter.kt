package com.bendezu.tinkofffintech.courses

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.data.entity.*
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.getUser
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import retrofit2.HttpException
import javax.inject.Inject

private const val TOP_STUDENTS = 10

@ActivityScope
class CoursesPresenter @Inject constructor(private val repository: CoursesRepository,
                                           private val sharedPreferences: SharedPreferences)
    : MvpBasePresenter<CoursesView>() {

    private val disposables = CompositeDisposable()

    fun loadData() {
        disposables += repository.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .subscribe(::onResult, ::onError) { ifViewAttached { it.setLoading(false) } }
    }

    private fun onResult(data: CoursesData) {
        ifViewAttached { it.setToolbarTitle(data.course.title) }
        setTopStudents(data.students)
        setRatingStats(data.tasks, data.lectures, data.students)
        if (data.course.title.isEmpty())
            ifViewAttached { it.setLoading(true) }
    }

    private fun setTopStudents(students: List<StudentEntity>) {
        val userId = sharedPreferences.getUser().id
        val badges = students
            .sortedByDescending { it.totalMark }
            .take(TOP_STUDENTS)
            .map { StudentBadge(it.id, it.name, it.totalMark, it.id == userId) }

        ifViewAttached { it.setTopStudents(badges) }
    }

    private fun setRatingStats(tasks: List<TaskEntity>, lectures: List<LectureEntity>, students: List<StudentEntity>) {
        val userId = sharedPreferences.getUser().id
        val sortedStudents = students.sortedByDescending { it.totalMark }

        val totalPoints = students.firstOrNull { it.id == userId }?.totalMark ?: 0f
        val maxTotalPoints = tasks.sumByDouble { it.maxScore.toDouble() }
        val totalLectures = lectures.size

        val userPosition = sortedStudents.indexOfFirst { it.id == userId } + 1
        val totalStudents = students.size

        val tests = tasks.filter { it.taskType == TaskType.TEST }
        val totalTests = tests.size
        val acceptedTests = tests.filter { it.status == TaskStatus.ACCEPTED }.size

        val homeworks = tasks.filter { it.taskType == TaskType.HOMEWORK }
        val totalHomeworks = homeworks.size
        val acceptedHomeworks = homeworks.filter { it.status == TaskStatus.ACCEPTED }.size

        val passedLectures = tasks
            .filter { it.deadlineDate != null }
            .map { it.lectureId to Instant.parse(it.deadlineDate).atZone(ZoneId.systemDefault()).toLocalDateTime() }
            .filter { LocalDateTime.now().isAfter(it.second) }
            .distinctBy { it.first }
            .size

        ifViewAttached { it.setRatingStats(RatingStats(
            totalPoints, maxTotalPoints,
            passedLectures, totalLectures,
            userPosition, totalStudents,
            acceptedTests, totalTests,
            acceptedHomeworks, totalHomeworks)
        ) }
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()
        ifViewAttached {
            it.setLoading(false)
            when (t) {
                is HttpException -> if (t.code() == 403) it.openAuthorizationActivity()
                else -> it.showNetworkError()
            }
        }
    }

    override fun destroy() {
        disposables.clear()
        super.destroy()
    }

}