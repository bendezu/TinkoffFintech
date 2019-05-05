package com.bendezu.tinkofffintech.courses.rating_details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.di.Injector
import kotlinx.android.synthetic.main.activity_rating_detail.*

class RatingDetailsActivity: AppCompatActivity(), LecturesFragment.Listener, TasksFragment.InjectorProvider {

    companion object {
        private const val STATE_TITLE = "title"
    }

    private val component = Injector.ratingDetailsComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setOnApplyWindowInsetsListener { _, insets ->
            statusBarBackground.layoutParams.height = insets.systemWindowInsetTop
            insets.consumeSystemWindowInsets()
        }

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LecturesFragment())
                .commit()
        else
            toolbar.title = savedInstanceState.getString(STATE_TITLE)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(STATE_TITLE, toolbar.title.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun setToolbarTitle(title: String) {
        toolbar.title = title
    }

    override fun inject(lecturesFragment: LecturesFragment) {
        component.inject(lecturesFragment)
    }

    override fun inject(tasksFragment: TasksFragment) {
        component.inject(tasksFragment)
    }
}