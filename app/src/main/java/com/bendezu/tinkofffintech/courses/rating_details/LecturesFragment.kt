package com.bendezu.tinkofffintech.courses.rating_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.courses.performance_details.ListItemDecoration
import com.bendezu.tinkofffintech.data.LectureEntity
import kotlinx.android.synthetic.main.fragment_lectures.*

class LecturesFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lectures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val lecturesAdapter = LecturesAdapter {
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }
        lecturesAdapter.data = listOf(
            LectureEntity(1L, "lecture1"),
            LectureEntity(2L, "lecture2"),
            LectureEntity(3L, "lecture3")
        )
        recycler.apply {
            adapter = lecturesAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ListItemDecoration(requireContext()).apply { margin = 32 })
        }
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
        }
    }
}