package com.bendezu.tinkofffintech

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

const val DIALOG_RESULT = 102

class ConfirmationDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(R.string.dialog_confirmation_message)
            .setPositiveButton(R.string.leave) { _, _ ->
                leave()
            }
            .setNegativeButton(R.string.stay) { _, _ ->
                stay()
            }
            .create()
    }



    override fun onCancel(dialog: DialogInterface?) {
        stay()
        super.onCancel(dialog)
    }

    private fun leave() {
        parentFragment?.onActivityResult(DIALOG_RESULT, Activity.RESULT_OK, requireActivity().intent)
    }

    private fun stay() {
        parentFragment?.onActivityResult(DIALOG_RESULT, Activity.RESULT_CANCELED, requireActivity().intent)
    }
}