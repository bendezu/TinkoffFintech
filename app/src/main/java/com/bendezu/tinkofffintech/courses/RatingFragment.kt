package com.bendezu.tinkofffintech.courses

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.courses.rating_details.RatingDetailsActivity
import kotlinx.android.synthetic.main.fragment_rating.*
import kotlin.math.floor

class RatingFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailsButton.setOnClickListener {
            startActivity(Intent(context, RatingDetailsActivity::class.java))
        }
    }

    fun setRatingStats(stats: RatingStats) {
        points.text = stats.totalPoints.toString()
        pointsLabel.text = requireContext().resources.getQuantityString(
            R.plurals.points, floor(stats.totalPoints).toInt()
        )
        totalRating.text = requireContext().getString(R.string.progress_numbers,
            stats.userPosition.toString(), stats.totalStudents.toString())
        testsCompleted.text = requireContext().getString(R.string.progress_numbers,
            stats.acceptedTests.toString(), stats.totalTests.toString())
        hwsCompleted.text = requireContext().getString(R.string.progress_numbers,
            stats.acceptedHomeworks.toString(), stats.totalHomeworks.toString())
        totalLessons.text = requireContext().resources.getQuantityString(
            R.plurals.lessons_and_value, stats.totalLectures, stats.totalLectures
        )
    }
}