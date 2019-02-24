package com.bendezu.tinkofffintech

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_edit_profile.*

private const val ARG_FIRST_NAME = "first_name"
private const val ARG_SECOND_NAME = "second_name"
private const val ARG_PATRONYMIC = "patronymic"

class EditProfileFragment : Fragment() {

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
        saveButton.setOnClickListener {
            activity?.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                ?.edit {
                    putString(PREF_FIRST_NAME, firstNameEditText.text.toString())
                    putString(PREF_SECOND_NAME, secondNameEditText.text.toString())
                    putString(PREF_PATRONYMIC, patronymicEditText.text.toString())
                }
            listener.onSaveButtonClicked()
        }
        cancelButton.setOnClickListener {
            listener.onCancelButtonClicked()
        }
        firstNameEditText.setText(arguments?.getString(ARG_FIRST_NAME))
        secondNameEditText.setText(arguments?.getString(ARG_SECOND_NAME))
        patronymicEditText.setText(arguments?.getString(ARG_PATRONYMIC))
    }
}