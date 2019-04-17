package com.bendezu.tinkofffintech.courses.rating_details

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.App
import com.bendezu.tinkofffintech.data.LectureDao
import com.bendezu.tinkofffintech.data.LectureEntity
import com.bendezu.tinkofffintech.data.TaskDao
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

class LecturesPresenter(lectureDao: LectureDao,
                        taskDao: TaskDao,
                        sharedPreferences: SharedPreferences = App.preferences) :
    MvpBasePresenter<LecturesView>(), HomeworksRepository.LecturesCallback {

    private val repository = HomeworksRepository(lectureDao, taskDao, sharedPreferences, this)

    fun loadData() {
        repository.getLectures()
    }

    override fun onResult(lectures: List<LectureEntity>, shouldStopLoading: Boolean) {
        ifViewAttached {
            it.showLectures(lectures)
            if (shouldStopLoading)
                it.setLoading(false)
            else
                if (lectures.isEmpty()) it.setLoading(true)
        }
    }

    override fun onError(t: Throwable) {
        when(t) {
            is NetworkException -> ifViewAttached { it.showNetworkError() }
            is UnauthorizedException -> ifViewAttached { it.openAuthorizationActivity() }
        }
    }
}