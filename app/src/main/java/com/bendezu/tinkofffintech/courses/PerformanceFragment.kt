package com.bendezu.tinkofffintech.courses

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.courses.performance_details.PerformanceDetailActivity
import kotlinx.android.synthetic.main.fragment_performance.*

class PerformanceFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_performance, container, false)
    }

    private val studentBadgesAdapter = StudentBadgesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailsButton.setOnClickListener {
            startActivity(Intent(context, PerformanceDetailActivity::class.java))
        }

        recycler.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = studentBadgesAdapter
        }

    }

    fun setTopStudents(students: List<StudentBadge>) {
        if (studentBadgesAdapter.itemCount == 0)
            TransitionManager.beginDelayedTransition(recycler)
        studentBadgesAdapter.data = students
        emptyList.visibility = if (studentBadgesAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }
}