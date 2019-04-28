package com.bendezu.tinkofffintech.courses.performance_details

import com.bendezu.tinkofffintech.data.StudentEntity
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

class StudentsPresenter(private val repository: StudentsRepository) : MvpBasePresenter<StudentsView>(),
    StudentsRepository.StudentsCallback {

    init {
        repository.callback = this
    }

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