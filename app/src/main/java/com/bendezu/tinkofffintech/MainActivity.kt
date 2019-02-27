package com.bendezu.tinkofffintech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_TITLE = "title"
private const val EDIT_PROFILE_FRAGMENT_TAG = "edit_profile_fragment"

interface BackButtonListener {
    fun onBackPressed()
}

class MainActivity : AppCompatActivity(), ProfileTabListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemSelectedListener {
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
            bottomNavigationView.selectedItemId = R.id.action_events
        else
            toolbar.title = savedInstanceState.getString(STATE_TITLE)
    }

    override fun onEditButtonClicked(firstName: String, secondName: String, patronymic: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, EditProfileFragment.newInstance(firstName, secondName, patronymic), EDIT_PROFILE_FRAGMENT_TAG)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(STATE_TITLE, toolbar.title.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        val editFragment = supportFragmentManager.findFragmentByTag(EDIT_PROFILE_FRAGMENT_TAG) as? BackButtonListener
        if (editFragment != null) {
            editFragment.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}
