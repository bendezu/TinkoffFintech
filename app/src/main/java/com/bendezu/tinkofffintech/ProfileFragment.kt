package com.bendezu.tinkofffintech

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment: Fragment() {

    private lateinit var listener: ProfileTabListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (activity !is ProfileTabListener) {
            throw ClassCastException(context.toString() + " must implement ProfileTabListener")
        }
        listener = activity as ProfileTabListener
    }

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
            listener.onEditButtonClicked(
                firstNameTextView.text.toString(),
                secondNameTextView.text.toString(),
                patronymicTextView.text.toString())
        }
    }
}