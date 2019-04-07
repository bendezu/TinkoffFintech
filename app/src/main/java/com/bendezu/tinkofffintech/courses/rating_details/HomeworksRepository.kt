package com.bendezu.tinkofffintech.courses.rating_details

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.bendezu.tinkofffintech.App
import com.bendezu.tinkofffintech.PREF_COOKIE
import com.bendezu.tinkofffintech.data.LectureDao
import com.bendezu.tinkofffintech.data.LectureEntity
import com.bendezu.tinkofffintech.data.TaskDao
import com.bendezu.tinkofffintech.data.TaskEntity
import com.bendezu.tinkofffintech.network.HomeworksResponse
import java.io.IOException
import kotlin.concurrent.thread

class NetworkException: RuntimeException()
class UnauthorizedException: RuntimeException()

class HomeworksRepository(private val lectureDao: LectureDao,
                          private val taskDao: TaskDao,
                          private val sharedPreferences: SharedPreferences,
                          var callback: LecturesCallback? = null) {

    interface LecturesCallback {
        fun onResult(lectures: List<LectureEntity>, fromNetwork: Boolean = false)
        fun onError(t: Throwable)
    }

    private val uiHandler = Handler(Looper.getMainLooper())

    fun getLectures() {
        thread {
            val dbLectures = lectureDao.getAll()
            uiHandler.post{ callback?.onResult(dbLectures) }

            val cookie = sharedPreferences.getString(PREF_COOKIE, "").orEmpty()
            try {
                val response = App.apiService.getHomeworks(cookie).execute()
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

    private fun HomeworksResponse.toEntities(): Pair<List<LectureEntity>, List<TaskEntity>> {
        val homeworks = this.homeworks
        val lectures = mutableListOf<LectureEntity>()
        val tasks = mutableListOf<TaskEntity>()
        for (homework in homeworks) {
            lectures.add(LectureEntity(homework.id, homework.title))
            for (task in homework.tasks) {
                val taskData = task.taskData
                tasks.add(TaskEntity(
                    taskData.id,
                    taskData.title,
                    taskData.taskType,
                    taskData.maxScore,
                    taskData.deadlineDate,
                    task.status,
                    task.mark,
                    homework.id)
                )
            }
        }
        return lectures to tasks
    }

}