package com.bendezu.tinkofffintech.courses.performance_details

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.bendezu.tinkofffintech.R
import kotlinx.android.synthetic.main.fragment_account_list.*

private const val REQUEST_CONTACT_PERMISSION = 213
private var index = 1

class AccountListFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var accountsAdapter: AccountsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        accountsAdapter = AccountsAdapter(GridLayoutManager(context, 1))
        recycler.apply {
            layoutManager = accountsAdapter.layoutManager
            adapter = accountsAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            addItemDecoration(DividerItemDecoration(context, LinearLayout.HORIZONTAL))
        }
        checkAndRequestPermission()
    }

    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CONTACT_PERMISSION
            )
        } else {
            // Permission has already been granted
            loadContacts()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        when (requestCode) {
            REQUEST_CONTACT_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    loadContacts()
                } else {
                    Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }
            }
        }
    }

    private fun loadContacts() {
        LoaderManager.getInstance(this).initLoader(0, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?) = CursorLoader(
        requireContext(),
        ContactsContract.Contacts.CONTENT_URI,
        arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY),
        null,
        null,
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
    )

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        updateAdapter(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        updateAdapter(null)
    }

    private fun updateAdapter(cursor: Cursor?) {
        val names = mutableListOf<String>()
        cursor?.let {
            for (i in 0 until cursor.count) {
                cursor.moveToPosition(i)
                val name = cursor.getString(
                    cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
                    )
                )
                names.add(name)
            }
        }
        accountsAdapter.data = names
    }

    fun switchView() {
        accountsAdapter.layoutManager.apply { spanCount = if (spanCount == 1) 3 else 1 }
        accountsAdapter.notifyItemRangeChanged(0, accountsAdapter.itemCount)
    }

    fun addAccount() {
        accountsAdapter.data.add("New Account ${index++}")
        accountsAdapter.notifyItemInserted(accountsAdapter.data.size)
    }

    fun removeAccount() {
        val index = accountsAdapter.data.lastIndex
        accountsAdapter.data.removeAt(index)
        accountsAdapter.notifyItemRemoved(index)
    }

    fun shuffleAccounts() {
        val shuffled = accountsAdapter.data.toMutableList().apply { shuffle() }
        accountsAdapter.data = shuffled
    }
}