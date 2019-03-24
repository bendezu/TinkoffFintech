package com.bendezu.tinkofffintech.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bendezu.tinkofffintech.*
import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.User
import com.bendezu.tinkofffintech.network.UserResponse
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

private const val EDIT_PROFILE_FRAGMENT_TAG = "edit_profile_fragment"

class ProfileFragment: Fragment(), Callback<UserResponse> {

    private lateinit var apiService: FintechApiService
    private var preferences: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        apiService = Retrofit.Builder()
            .baseUrl(FintechApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()
        preferences = activity?.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        val user = getUserFromPref()
        setUser(user)

        val cookie = preferences?.getString(PREF_COOKIE, "").orEmpty()
        if (user.firstname.isEmpty() || user.lastname.isEmpty()) {
            apiService.getUser(cookie).enqueue(this)
        }

        editButton.setOnClickListener {
            val editFragment = EditProfileFragment.newInstance(
                firstNameTextView.text.toString(),
                secondNameTextView.text.toString(),
                patronymicTextView.text.toString()
            )
            fragmentManager?.beginTransaction()
                ?.replace(
                    R.id.container, editFragment,
                    EDIT_PROFILE_FRAGMENT_TAG
                )
                ?.addToBackStack(null)
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ?.commit()
        }
    }

    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
        Toast.makeText(requireContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
        if (response.isSuccessful) {
            val user = response.body()?.user
            if (user != null) {
                setUser(user)
                saveUserToPrefs(user)
            }
        } else {
            openAuthorizationActivity()
        }
    }

    private fun setUser(user: User) {
        firstNameTextView.text = user.firstname
        secondNameTextView.text = user.lastname
        patronymicTextView.text = user.middlename
    }

    private fun saveUserToPrefs(user: User) {
        preferences?.edit {
            putString(PREF_FIRST_NAME, user.firstname)
            putString(PREF_SECOND_NAME, user.lastname)
            putString(PREF_PATRONYMIC, user.middlename)
            putString(PREF_AVATAR, user.avatar)
        }
    }

    private fun getUserFromPref(): User {
        val firstName = preferences?.getString(PREF_FIRST_NAME, "").orEmpty()
        val secondName = preferences?.getString(PREF_SECOND_NAME, "").orEmpty()
        val patronymic = preferences?.getString(PREF_PATRONYMIC, "").orEmpty()
        val avatar = preferences?.getString(PREF_AVATAR, "").orEmpty()
        return User("", firstName, secondName, patronymic, avatar)
    }

    private fun openAuthorizationActivity() {
        //reset cookie
        preferences?.edit {
            putString(PREF_COOKIE, null)
            putString(PREF_EXPIRES, null)
        }
        startActivity(Intent(context, AuthorizationActivity::class.java))
        activity?.finish()
    }
}