package com.bendezu.tinkofffintech.profile

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import com.bendezu.tinkofffintech.R
import com.google.android.material.textfield.TextInputLayout

class TextValidator(private val inputLayout: TextInputLayout, private val context: Context): TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        val text = s?.toString().orEmpty()
        when {
            text.isEmpty() -> {
                inputLayout.error = context.getString(R.string.empty_error_message)
            }
            text.first().isLowerCase() || text.first().isDigit() -> {
                inputLayout.error = context.getString(R.string.lowercase_error_message)
            }
            else -> {
                inputLayout.error = null
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

}