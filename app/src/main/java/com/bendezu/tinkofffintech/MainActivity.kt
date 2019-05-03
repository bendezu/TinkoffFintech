package com.bendezu.tinkofffintech

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.bendezu.tinkofffintech.courses.CoursesFragment
import com.bendezu.tinkofffintech.di.Injector
import com.bendezu.tinkofffintech.events.EventsFragment
import com.bendezu.tinkofffintech.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

interface BackButtonListener {
    fun onBackPressed()
}

class MainActivity : AppCompatActivity(), ProfileFragment.InjectorProvider,
                                          EventsFragment.InjectorProvider,
                                          CoursesFragment.InjectorProvider {

    private val component = Injector.mainActivityComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            if (bottomNavigationView.selectedItemId == it.itemId)
                return@setOnNavigationItemSelectedListener true

            val fragment = when (it.itemId) {
                R.id.action_events -> EventsFragment()
                R.id.action_courses -> CoursesFragment()
                R.id.action_profile -> ProfileFragment()
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
            return@setOnNavigationItemSelectedListener true
        }
        if (savedInstanceState == null)
            bottomNavigationView.selectedItemId = R.id.action_courses
    }

    override fun onBackPressed() {
        val visibleFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (visibleFragment is BackButtonListener)
            visibleFragment.onBackPressed()
        else
            super.onBackPressed()
    }

    override fun inject(profileFragment: ProfileFragment) {
        component.inject(profileFragment)
    }

    override fun inject(eventsFragment: EventsFragment) {
        component.inject(eventsFragment)
    }

    override fun inject(coursesFragment: CoursesFragment) {
        component.inject(coursesFragment)
    }
}
