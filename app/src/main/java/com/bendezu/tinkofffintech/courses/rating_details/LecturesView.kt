package com.bendezu.tinkofffintech.courses.rating_details

import com.bendezu.tinkofffintech.data.entity.LectureEntity
import com.hannesdorfmann.mosby3.mvp.MvpView

interface LecturesView: MvpView {
    fun showLectures(lectures: List<LectureEntity>)
    fun showNetworkError()
    fun openAuthorizationActivity()
    fun setLoading(loading: Boolean)
}