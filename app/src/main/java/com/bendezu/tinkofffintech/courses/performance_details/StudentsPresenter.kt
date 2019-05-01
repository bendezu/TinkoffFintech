package com.bendezu.tinkofffintech.courses.performance_details

import com.bendezu.tinkofffintech.data.entity.StudentEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import javax.inject.Inject

@ActivityScope
class StudentsPresenter @Inject constructor(private val repository: StudentsRepository) : MvpBasePresenter<StudentsView>(),
    StudentsRepository.StudentsCallback {

    init {
        repository.callback = this
    }

    fun loadData() {
        repository.getStudents()
    }

    override fun destroy() {
        repository.dispose()
        super.destroy()
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
        ifViewAttached {
            it.setLoading(false)
            when (t) {
                is NetworkException -> it.showNetworkError()
                is UnauthorizedException -> it.openAuthorizationActivity()
            }
        }
    }
}