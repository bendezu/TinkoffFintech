package com.bendezu.tinkofffintech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_SELECTED_ITEM = "selected_item"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.action_events -> EventsFragment()
                R.id.action_courses -> CoursesFragment()
                R.id.action_profile -> ProfileFragment()
                else -> throw IllegalArgumentException("Unknown item selected: $it")
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
            true
        }
        val selectedItem = savedInstanceState?.getInt(STATE_SELECTED_ITEM) ?: R.id.action_events
        bottomNavigationView.selectedItemId = selectedItem
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle?) {
        outState.putInt(STATE_SELECTED_ITEM, bottomNavigationView.selectedItemId)
        super.onSaveInstanceState(outState, outPersistentState)
    }
}
