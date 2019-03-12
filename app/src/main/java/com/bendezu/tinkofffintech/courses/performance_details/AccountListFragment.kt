package com.bendezu.tinkofffintech.courses.performance_details

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import com.bendezu.tinkofffintech.R
import kotlinx.android.synthetic.main.fragment_account_list.*

private const val REQUEST_CONTACT_PERMISSION = 213
private var index = 1

class AccountListFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var accountsAdapter: AccountsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (accountsAdapter == null) {
            accountsAdapter = AccountsAdapter(GridLayoutManager(context, 1))
        } else {
            accountsAdapter?.layoutManager = GridLayoutManager(context, accountsAdapter?.layoutManager?.spanCount ?: 1)
        }
        recycler.apply {
            layoutManager = accountsAdapter?.layoutManager
            adapter = accountsAdapter
            addItemDecoration(ListItemDecoration(context))
        }
        if (savedInstanceState == null)
            checkAndRequestPermission()
    }

    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
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
        accountsAdapter?.data = names
        checkTransactionsCount()
    }

    private fun checkTransactionsCount() {
        emptyList.visibility = if (accountsAdapter?.itemCount == 0) View.VISIBLE else View.GONE
    }

    fun switchView() {
        for (i in recycler.itemDecorationCount - 1 downTo  0)
            recycler.removeItemDecorationAt(i)
        if (accountsAdapter?.layoutManager?.spanCount == 1) {
            accountsAdapter?.layoutManager?.spanCount = 3
            recycler.addItemDecoration(GridItemDecoration(requireContext(), 3))
        } else {
            accountsAdapter?.layoutManager?.spanCount = 1
            recycler.addItemDecoration(ListItemDecoration(requireContext()))
        }
        accountsAdapter?.notifyItemRangeChanged(0, accountsAdapter?.itemCount ?: 0)
    }

    fun addAccount() {
        accountsAdapter?.apply {
            data.add("New Account ${index++}")
            notifyItemInserted(data.size)
        }
        checkTransactionsCount()
    }

    fun removeAccount() {
        if (accountsAdapter?.itemCount == 0) return
        accountsAdapter?.apply {
            val index = data.lastIndex
            data.removeAt(index)
            notifyItemRemoved(index)
        }
        checkTransactionsCount()
    }

    fun shuffleAccounts() {
        accountsAdapter?.apply {
            val shuffled = data.toMutableList().apply { shuffle() }
            data = shuffled
        }
    }
}