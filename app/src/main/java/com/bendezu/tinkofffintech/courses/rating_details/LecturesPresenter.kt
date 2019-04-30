package com.bendezu.tinkofffintech.courses.rating_details

import com.bendezu.tinkofffintech.data.LectureEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import javax.inject.Inject

@ActivityScope
class LecturesPresenter @Inject constructor(private val repository: HomeworksRepository) :
    MvpBasePresenter<LecturesView>(), HomeworksRepository.LecturesCallback {

    init {
        repository.callback = this
    }

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