package com.bendezu.tinkofffintech.courses.course_details

import com.bendezu.tinkofffintech.network.models.CourseDetails
import com.hannesdorfmann.mosby3.mvp.MvpView

interface CourseDetailsView : MvpView {

    fun showData(courseDetails: CourseDetails)
    fun showNetworkError()
    fun openAuthorizationActivity()
    fun setLoading(loading: Boolean)

}