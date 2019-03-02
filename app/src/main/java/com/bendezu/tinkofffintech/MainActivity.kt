package com.bendezu.tinkofffintech

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.bendezu.tinkofffintech.courses.CoursesFragment
import com.bendezu.tinkofffintech.events.EventsFragment
import com.bendezu.tinkofffintech.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_TITLE = "title"

interface BackButtonListener {
    fun onBackPressed()
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            if (bottomNavigationView.selectedItemId == it.itemId)
                return@setOnNavigationItemSelectedListener true

            val fragment = when (it.itemId) {
                R.id.action_events -> {
                    toolbar.setTitle(R.string.events)
                    EventsFragment()
                }
                R.id.action_courses -> {
                    toolbar.setTitle(R.string.courses)
                    CoursesFragment()
                }
                R.id.action_profile -> {
                    toolbar.setTitle(R.string.profile)
                    ProfileFragment()
                }
                else -> throw IllegalArgumentException("Unknown item selected: $it")
            }
            //clear back stack
            for (i in 0 until supportFragmentManager.backStackEntryCount) {
                supportFragmentManager.popBackStackImmediate()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
            true
        }
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EventsFragment())
                .commit()
        else
            toolbar.title = savedInstanceState.getString(STATE_TITLE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(STATE_TITLE, toolbar.title.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        val visibleFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (visibleFragment is BackButtonListener)
            visibleFragment.onBackPressed()
        else
            super.onBackPressed()
    }
}
