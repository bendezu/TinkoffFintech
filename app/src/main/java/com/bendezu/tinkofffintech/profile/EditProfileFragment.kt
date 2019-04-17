package com.bendezu.tinkofffintech.profile

import android.content.Context
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

class EditProfileFragment : Fragment(), BackButtonListener, ConfirmationListener {

    private lateinit var initialUser: User

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
        super.onViewCreated(view, savedInstanceState)

        initialUser = App.preferences.getUser()

        val avatar = initialUser.avatar
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

        firstNameEditText.setText(initialUser.firstname)
        secondNameEditText.setText(initialUser.lastname)
        patronymicEditText.setText(initialUser.middlename)

        saveButton.setOnClickListener {
            val user = User( initialUser.email,
                firstNameEditText.text.toString(),
                secondNameEditText.text.toString(),
                patronymicEditText.text.toString(),
                initialUser.avatar)
            App.preferences.saveUser(user)
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
            val initials = "${initialUser.firstname} ${initialUser.lastname}".getInitials()
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
        if (firstNameEditText.text.toString() != initialUser.firstname ||
            secondNameEditText.text.toString() != initialUser.lastname ||
            patronymicEditText.text.toString() != initialUser.middlename) {
            ConfirmationDialog().show(childFragmentManager, null)
        } else {
            fragmentManager?.popBackStack()
        }
    }
}