package com.bendezu.tinkofffintech.courses.performance_details

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.App
import com.bendezu.tinkofffintech.data.StudentDao
import com.bendezu.tinkofffintech.data.StudentEntity
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

class AccountsPresenter(studentDao: StudentDao, preferences: SharedPreferences = App.preferences) : MvpBasePresenter<AccountsView>(),
    StudentsRepository.StudentsCallback {

    private val repository = StudentsRepository(studentDao, preferences, this)

    fun loadData() {
        repository.getStudents()
    }

    override fun onResult(students: List<StudentEntity>, shouldStopLoading: Boolean) {
        ifViewAttached {
            it.showStudents(students)
            if (shouldStopLoading)
                it.setLoading(false)
            else
                if (students.isEmpty()) it.setLoading(true)
        }
    }

    override fun onError(t: Throwable) {
        when (t) {
            is NetworkException -> ifViewAttached { it.showNetworkError() }
            is UnauthorizedException -> ifViewAttached { it.openAuthorizationActivity() }
        }
    }
}