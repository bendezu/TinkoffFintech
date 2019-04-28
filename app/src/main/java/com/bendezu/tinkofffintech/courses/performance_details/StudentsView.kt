package com.bendezu.tinkofffintech.courses.performance_details

import com.bendezu.tinkofffintech.data.StudentEntity
import com.hannesdorfmann.mosby3.mvp.MvpView

interface StudentsView: MvpView {
    fun showStudents(students: List<StudentEntity>)
    fun setLoading(loading: Boolean)
    fun showNetworkError()
    fun openAuthorizationActivity()
    fun checkAccountsCount()
}