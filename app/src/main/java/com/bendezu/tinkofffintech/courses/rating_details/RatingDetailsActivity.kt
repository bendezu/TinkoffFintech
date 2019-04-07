package com.bendezu.tinkofffintech.courses.rating_details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bendezu.tinkofffintech.R
import kotlinx.android.synthetic.main.activity_rating_detail.*

class RatingDetailsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LecturesFragment())
                .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            onBackPressed()
        }
        return true
    }
}