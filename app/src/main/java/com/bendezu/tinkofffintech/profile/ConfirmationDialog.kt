package com.bendezu.tinkofffintech.profile

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bendezu.tinkofffintech.R

const val DIALOG_RESULT = 102

interface ConfirmationListener {
    fun onConfirm()
    fun onCancel()
}

class ConfirmationDialog : DialogFragment() {

    private lateinit var listener: ConfirmationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when {
            parentFragment is ConfirmationListener -> parentFragment as ConfirmationListener
            context is ConfirmationListener -> context
            else -> throw ClassCastException("Must implement ConfirmationListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(R.string.dialog_confirmation_message)
            .setPositiveButton(R.string.leave) { _, _ ->
                listener.onConfirm()
            }
            .setNegativeButton(R.string.stay) { _, _ ->
                listener.onCancel()
            }
            .create()
    }

    override fun onCancel(dialog: DialogInterface?) {
        listener.onCancel()
        super.onCancel(dialog)
    }
}