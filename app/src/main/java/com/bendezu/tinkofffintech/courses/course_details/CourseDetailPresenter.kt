package com.bendezu.tinkofffintech.courses.course_details

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.PREF_COURSE_URL
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.getCourse
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

@ActivityScope
class CourseDetailPresenter @Inject constructor(private val repository: CourseDetailRepository,
                                                private val preferences: SharedPreferences)
    : MvpBasePresenter<CourseDetailsView>() {

    private val disposables = CompositeDisposable()

    fun loadData() {
        val saved = repository.getSavedData()
        if (saved.html.isEmpty() && saved.title.isEmpty()) {

            if (preferences.getCourse().url.isEmpty()) {
                preferences.registerOnSharedPreferenceChangeListener { _, key ->
                    if (key == PREF_COURSE_URL) loadFromNetwork()
                }
            } else {
                loadFromNetwork()
            }

        } else {
            ifViewAttached { it.showData(saved) }
        }
    }

    fun loadFromNetwork() {
        ifViewAttached { it.setLoading(true) }
        disposables += repository.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ courseDetails -> ifViewAttached {
                it.setLoading(false)
                it.showData(courseDetails) }
            }, ::onError)
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