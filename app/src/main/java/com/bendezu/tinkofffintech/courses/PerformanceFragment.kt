package com.bendezu.tinkofffintech.courses

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.courses.performance_details.PerformanceDetailActivity
import com.bendezu.tinkofffintech.view.AccountBadgeView
import kotlinx.android.synthetic.main.fragment_performance.*
import java.lang.ref.WeakReference
import java.util.*

class PerformanceFragment: Fragment() {

    class AccountBadge(val name: String, var points: Int, @ColorRes val colorRes: Int, val highlighted: Boolean = false)
    val accounts = Collections.synchronizedList(arrayListOf(
        AccountBadge("Андрей", 9, R.color.colorPassed),
        AccountBadge("Вы", 7, R.color.colorPrimary, true),
        AccountBadge("Александр", 10, R.color.colorSecondAccent),
        AccountBadge("Светлана", 0, R.color.colorPrimaryLight),
        AccountBadge("Геннадий", 6, android.R.color.holo_purple)
    ))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_performance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailsButton.setOnClickListener {
            startActivity(Intent(context, PerformanceDetailActivity::class.java))
        }
        setupAccounts()
    }

    private fun setupAccounts() {
        accounts.sortByDescending { it.points }
        for (i in 0 until accounts.size) {
            val child = accountsLayout.getChildAt(i) as AccountBadgeView
            val data = accounts[i]
            child.name = data.name
            child.points = data.points.toString()
            child.avatarRes = data.colorRes
            child.highlighted = data.highlighted
            child.bagdeVisibility = if (data.points == 0) View.GONE else View.VISIBLE
        }
    }

    fun regenerateAccounts() {
        RandomizeAccountThread(WeakReference(this)).start()
    }

    fun updateAccounts() {
        (parentFragment as AccountsListener).onAccountsUpdated()
        if (view != null)
            setupAccounts()
    }
}