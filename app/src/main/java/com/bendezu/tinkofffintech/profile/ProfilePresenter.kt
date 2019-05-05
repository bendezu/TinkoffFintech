package com.bendezu.tinkofffintech.profile

import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.models.User
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.HttpException
import javax.inject.Inject

@ActivityScope
class ProfilePresenter @Inject constructor(private val repository: ProfileRepository) :
    MvpBasePresenter<ProfileView>() {

    private val disposables = CompositeDisposable()

    fun loadData() {
        disposables += repository.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .subscribe(::onResult, ::onError) { ifViewAttached { it.setLoading(false) } }
    }

    private fun onResult(user: User) {
        ifViewAttached {
            it.showUserProfile(user)
            if (user.email.isEmpty()) it.setLoading(true)
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

    fun getAgeByBirthday(birthday: String?): Int {
        val birthDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        return Period.between(birthDate, LocalDate.now()).years
    }

    override fun destroy() {
        disposables.clear()
        super.destroy()
    }
}
