package com.bendezu.tinkofffintech

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_edit_profile.*

private const val ARG_FIRST_NAME = "first_name"
private const val ARG_SECOND_NAME = "second_name"
private const val ARG_PATRONYMIC = "patronymic"

interface ProfileTabListener {
    fun onEditButtonClicked(firstName: String, secondName: String, patronymic: String)
}

class EditProfileFragment : Fragment(), BackButtonListener {

    companion object {
        fun newInstance(firstName: String, secondName: String, patronymic: String): EditProfileFragment {
            return EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FIRST_NAME, firstName)
                    putString(ARG_SECOND_NAME, secondName)
                    putString(ARG_PATRONYMIC, patronymic)
                }
            }
        }
    }

    private lateinit var listener: ProfileTabListener
    private lateinit var initialFirstName: String
    private lateinit var initialSecondName: String
    private lateinit var initialPatronymic: String

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (activity !is ProfileTabListener) {
            throw ClassCastException(context.toString() + " must implement ProfileTabListener")
        }
        listener = activity as ProfileTabListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialFirstName = arguments?.getString(ARG_FIRST_NAME).orEmpty()
        initialSecondName = arguments?.getString(ARG_SECOND_NAME).orEmpty()
        initialPatronymic = arguments?.getString(ARG_PATRONYMIC).orEmpty()

        firstNameEditText.addTextChangedListener(TextValidator(firstNameInputLayout, saveButton))
        secondNameEditText.addTextChangedListener(TextValidator(secondNameInputLayout, saveButton))
        patronymicEditText.addTextChangedListener(TextValidator(patronymicInputLayout, saveButton))

        firstNameEditText.setText(initialFirstName)
        secondNameEditText.setText(initialSecondName)
        patronymicEditText.setText(initialPatronymic)

        saveButton.setOnClickListener {
            activity?.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                ?.edit(true){
                    putString(PREF_FIRST_NAME, firstNameEditText.text.toString())
                    putString(PREF_SECOND_NAME, secondNameEditText.text.toString())
                    putString(PREF_PATRONYMIC, patronymicEditText.text.toString())
                }
            fragmentManager?.popBackStack()
        }
        cancelButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            DIALOG_RESULT -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        // leave without saving
                        fragmentManager?.popBackStack()
                    }
                    Activity.RESULT_CANCELED -> { /* stay and do nothing */ }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (firstNameEditText.text.toString() != initialFirstName ||
            secondNameEditText.text.toString() != initialSecondName ||
            patronymicEditText.text.toString() != initialPatronymic) {
            ConfirmationDialog().show(childFragmentManager, null)
        } else {
            fragmentManager?.popBackStack()
        }
    }
}