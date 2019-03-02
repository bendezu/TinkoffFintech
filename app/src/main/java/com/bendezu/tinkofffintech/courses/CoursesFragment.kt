package com.bendezu.tinkofffintech.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bendezu.tinkofffintech.R

class CoursesFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.performance_container, PerformanceFragment())
            ?.replace(R.id.rating_container, RatingFragment())
            ?.replace(R.id.completed_courses_container, CompletedCoursesFragment())
            ?.commit()
    }
}