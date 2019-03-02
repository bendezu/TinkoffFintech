package com.bendezu.tinkofffintech

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_profile.*

private const val EDIT_PROFILE_FRAGMENT_TAG = "edit_profile_fragment"

class ProfileFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val preferences = activity?.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val firstName = preferences?.getString(PREF_FIRST_NAME, "").orEmpty()
        val secondName = preferences?.getString(PREF_SECOND_NAME, "").orEmpty()
        val patronymic = preferences?.getString(PREF_PATRONYMIC, "").orEmpty()

        firstNameTextView.text = firstName
        secondNameTextView.text = secondName
        patronymicTextView.text = patronymic

        editButton.setOnClickListener {
            val editFragment = EditProfileFragment.newInstance(
                firstNameTextView.text.toString(),
                secondNameTextView.text.toString(),
                patronymicTextView.text.toString()
            )
            fragmentManager?.beginTransaction()
                ?.replace(R.id.container, editFragment, EDIT_PROFILE_FRAGMENT_TAG)
                ?.addToBackStack(null)
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ?.commit()
        }
    }
}