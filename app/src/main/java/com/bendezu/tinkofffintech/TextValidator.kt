package com.bendezu.tinkofffintech

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.android.material.textfield.TextInputLayout

class TextValidator(private val inputLayout: TextInputLayout, private val toggleView: View): TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        val text = s?.toString().orEmpty()
        when {
            text.isEmpty() -> {
                inputLayout.error = "Should not be empty"
                toggleView.isEnabled = false
            }
            text.first().isLowerCase() -> {
                inputLayout.error = "Should start with capital letter"
                toggleView.isEnabled = false
            }
            else -> {
                inputLayout.error = null
                toggleView.isEnabled = true
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

}