package com.bendezu.tinkofffintech.courses.rating_details

import com.bendezu.tinkofffintech.data.entity.LectureEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

@ActivityScope
class LecturesPresenter @Inject constructor(private val repository: HomeworksRepository) :
    MvpBasePresenter<LecturesView>() {

    private val disposables = CompositeDisposable()

    fun loadData() {
        disposables += repository.getLectures()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .subscribe(::onResult, ::onError) { ifViewAttached { it.setLoading(false) } }
    }

    private fun onResult(lectures: List<LectureEntity>) {
        ifViewAttached {
            it.showLectures(lectures)
            if (lectures.isEmpty()) it.setLoading(true)
        }
    }

    private fun onError(t: Throwable) {
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