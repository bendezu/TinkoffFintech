package com.bendezu.tinkofffintech.courses.performance_details

import android.content.Context
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.data.entity.StudentEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

@ActivityScope
class StudentsPresenter @Inject constructor(private val repository: StudentsRepository,
                                            private val context: Context) : MvpBasePresenter<StudentsView>() {

    private val disposables = CompositeDisposable()

    fun loadData() {
        disposables += repository.getStudents()
            .map { pair ->
                val (user, students) = pair
                for (student in students)
                    if (student.id == user.id) {
                        student.name = context.getString(R.string.you_with_name, student.name)
                        break
                    }
                return@map students
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .subscribe(::onResult, ::onError) { ifViewAttached { it.setLoading(false) } }
    }

    override fun destroy() {
        disposables.clear()
        super.destroy()
    }

    private fun onResult(students: List<StudentEntity>) {
        ifViewAttached {
            it.showStudents(students)
            if (students.isEmpty()) it.setLoading(true)
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
}