package com.bendezu.tinkofffintech.courses.rating_details

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.bendezu.tinkofffintech.data.dao.LectureDao
import com.bendezu.tinkofffintech.data.dao.TaskDao
import com.bendezu.tinkofffintech.data.entity.LectureEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.getCookie
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.bendezu.tinkofffintech.network.models.toEntities
import java.io.IOException
import javax.inject.Inject
import kotlin.concurrent.thread

@ActivityScope
class HomeworksRepository @Inject constructor(private val lectureDao: LectureDao,
                                              private val taskDao: TaskDao,
                                              private val sharedPreferences: SharedPreferences,
                                              private val apiService: FintechApiService) {

    var callback: LecturesCallback? = null

    interface LecturesCallback {
        fun onResult(lectures: List<LectureEntity>, shouldStopLoading: Boolean = false)
        fun onError(t: Throwable)
    }

    private val uiHandler = Handler(Looper.getMainLooper())

    fun getLectures() {
        thread {
            val dbLectures = lectureDao.getAll()
            uiHandler.post{ callback?.onResult(dbLectures) }

            val cookie = sharedPreferences.getCookie()
            try {
                val response = apiService.getHomeworks(cookie).execute()
                if (response.isSuccessful) {
                    val homeworks = response.body()
                    if (homeworks != null) {
                        val (netLectures, tasks) = homeworks.toEntities()
                        val sortedLectures = netLectures.sortedByDescending { it.id }

                        uiHandler.post{ callback?.onResult(sortedLectures, true) }

                        lectureDao.updateData(netLectures)
                        taskDao.insertAll(tasks)
                    }
                } else {
                    uiHandler.post{ callback?.onError(UnauthorizedException()) }
                }
            } catch (e: IOException) {
                uiHandler.post{ callback?.onError(NetworkException()) }
            }
        }
    }
}