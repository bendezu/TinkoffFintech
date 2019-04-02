package com.bendezu.tinkofffintech.courses.rating_details

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.data.FintechDatabase
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlin.concurrent.thread

private const val ARG_LECTURE_ID = "lecture_id"

class TasksFragment: Fragment() {

    companion object {
        fun newInstance(lectureId: Long): TasksFragment {
            return TasksFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_LECTURE_ID, lectureId)
                }
            }
        }
    }

    private var lectureId = 0L
    private val adapter = TasksAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lectureId = arguments?.getLong(ARG_LECTURE_ID) ?: 0
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)
        loadData()
    }

    private fun loadData() {
        thread {
            val tasks = FintechDatabase.getInstance(requireContext()).taskDao().getByLecture(lectureId)
            Handler(Looper.getMainLooper()).post {
                adapter.data = tasks
                emptyList.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
            }
        }
    }
}