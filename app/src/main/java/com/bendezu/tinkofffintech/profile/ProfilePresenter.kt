package com.bendezu.tinkofffintech.profile

import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.bendezu.tinkofffintech.network.User
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@ActivityScope
class ProfilePresenter @Inject constructor(private val repository: ProfileRepository) :
    MvpBasePresenter<ProfileView>(), ProfileRepository.ProfileCallback {

    init {
        repository.callback = this
    }

    fun loadData() {
        repository.getUser()
    }

    override fun onResult(user: User, shouldStopLoading: Boolean) {
        ifViewAttached {
            it.showUserProfile(user)
            if (shouldStopLoading)
                it.setLoading(false)
            else
                if (user.isEmpty()) it.setLoading(true)
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

    fun getAgeByBirthday(birthday: String?): Int {
        val birthDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        return Period.between(birthDate, LocalDate.now()).years
    }
}

private fun User.isEmpty(): Boolean {
    return this.email.isEmpty()
}
