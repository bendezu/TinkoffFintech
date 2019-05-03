package com.bendezu.tinkofffintech.courses

import com.bendezu.tinkofffintech.data.entity.StudentEntity
import com.hannesdorfmann.mosby3.mvp.MvpView

interface CoursesView: MvpView {
    fun setToolbarTitle(title:String)
    fun setTopStudents(students: List<StudentEntity>)
    fun setRatingStats(stats: RatingStats)
    fun showNetworkError()
    fun openAuthorizationActivity()
    fun setLoading(loading: Boolean)
}