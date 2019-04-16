package com.bendezu.tinkofffintech.profile

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.bendezu.tinkofffintech.*
import com.bendezu.tinkofffintech.network.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_edit_profile.*

private const val ARG_FIRST_NAME = "first_name"
private const val ARG_SECOND_NAME = "second_name"
private const val ARG_PATRONYMIC = "patronymic"

class EditProfileFragment : Fragment(), BackButtonListener, ConfirmationListener {

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

    private lateinit var initialFirstName: String
    private lateinit var initialSecondName: String
    private lateinit var initialPatronymic: String

    private lateinit var preferences: SharedPreferences

    private val saveButtonTextWatcher = object :TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            saveButton.isEnabled = firstNameInputLayout.error.isNullOrEmpty() &&
                    secondNameInputLayout.error.isNullOrEmpty() &&
                    patronymicInputLayout.error.isNullOrEmpty()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialFirstName = arguments?.getString(ARG_FIRST_NAME).orEmpty()
        initialSecondName = arguments?.getString(ARG_SECOND_NAME).orEmpty()
        initialPatronymic = arguments?.getString(ARG_PATRONYMIC).orEmpty()

        preferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        val avatar = preferences.getAvatar()
        setAvatarImage(avatar)

        firstNameEditText.addTextChangedListener(
            TextValidator(
                firstNameInputLayout,
                requireContext()
            )
        )
        secondNameEditText.addTextChangedListener(
            TextValidator(
                secondNameInputLayout,
                requireContext()
            )
        )
        patronymicEditText.addTextChangedListener(
            TextValidator(
                patronymicInputLayout,
                requireContext()
            )
        )

        firstNameEditText.addTextChangedListener(saveButtonTextWatcher)
        secondNameEditText.addTextChangedListener(saveButtonTextWatcher)
        patronymicEditText.addTextChangedListener(saveButtonTextWatcher)

        firstNameEditText.setText(initialFirstName)
        secondNameEditText.setText(initialSecondName)
        patronymicEditText.setText(initialPatronymic)

        saveButton.setOnClickListener {
            val user = User( "",
                firstNameEditText.text.toString(),
                secondNameEditText.text.toString(),
                patronymicEditText.text.toString())
            preferences.saveUser(user)
            fragmentManager?.popBackStack()
        }
        cancelButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onConfirm() {
        hideSoftKeyboard()
        // leave without saving
        fragmentManager?.popBackStack()
    }

    override fun onCancel() {
        /* stay and do nothing */
    }

    private fun setAvatarImage(avatar: String?) {
        if (avatar == null) {
            val initials = "$initialFirstName $initialSecondName".getInitials()
            avatarImageView.setImageDrawable(ColorDrawable(initials.getAvatarColor()))
            avatarImageView.initials = initials
        } else {
            val avatarUrl = "https://fintech.tinkoff.ru$avatar"
            Glide.with(this).load(avatarUrl).into(avatarImageView)
        }
    }

    private fun hideSoftKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val view: View? = activity?.findViewById(R.id.container)
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
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