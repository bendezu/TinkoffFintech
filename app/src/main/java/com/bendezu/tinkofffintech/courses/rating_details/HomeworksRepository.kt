package com.bendezu.tinkofffintech.courses.rating_details

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.data.dao.LectureDao
import com.bendezu.tinkofffintech.data.dao.TaskDao
import com.bendezu.tinkofffintech.data.entity.LectureEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.getCookie
import com.bendezu.tinkofffintech.getCourse
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.models.toEntities
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

@ActivityScope
class HomeworksRepository @Inject constructor(private val lectureDao: LectureDao,
                                              private val taskDao: TaskDao,
                                              private val sharedPreferences: SharedPreferences,
                                              private val apiService: FintechApiService) {

    fun getLectures(): Flowable<List<LectureEntity>> {
        val cookie = sharedPreferences.getCookie()
        val course = sharedPreferences.getCourse()
        val homeworksSingle = if (course.url.isEmpty()) {
            apiService.getConnectionsRx(cookie)
                .flatMap { apiService.getHomeworksRx(cookie, it.courses[0].url) }
        } else {
            apiService.getHomeworksRx(cookie, course.url)
        }
        val networkSource = homeworksSingle.map { homeworks ->
            val (netLectures, tasks) = homeworks.toEntities()
            val sortedLectures = netLectures.sortedByDescending { it.id }
            lectureDao.updateData(netLectures)
            taskDao.insertAll(tasks)
            return@map sortedLectures
        }
        return Single.concat(lectureDao.getAllRx(), networkSource)
    }
}