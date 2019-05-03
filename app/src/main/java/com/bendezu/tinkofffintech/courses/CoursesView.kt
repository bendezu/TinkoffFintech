package com.bendezu.tinkofffintech.courses

import com.hannesdorfmann.mosby3.mvp.MvpView

interface CoursesView: MvpView {
    fun setToolbarTitle(title:String)
    fun setTopStudents(students: List<StudentBadge>)
    fun setRatingStats(stats: RatingStats)
    fun showNetworkError()
    fun openAuthorizationActivity()
    fun setLoading(loading: Boolean)
}