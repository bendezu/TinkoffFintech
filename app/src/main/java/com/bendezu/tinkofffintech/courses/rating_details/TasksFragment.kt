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
import com.bendezu.tinkofffintech.data.TaskEntity
import kotlinx.android.synthetic.main.fragment_tasks.*
import java.lang.ref.WeakReference

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
    private val tasksAdapter = TasksAdapter()
    private lateinit var db: FintechDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = FintechDatabase.getInstance(requireContext())
        lectureId = arguments?.getLong(ARG_LECTURE_ID) ?: 0
        recycler.apply {
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(context)
        }
        TasksThread(WeakReference(this)).start()
    }

    private fun setData(tasks: List<TaskEntity>) {
        tasksAdapter.data = tasks
        emptyList.visibility = if (tasksAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    class TasksThread(private val tasksFragment: WeakReference<TasksFragment>): Thread() {
        override fun run() {
            val fragment = tasksFragment.get()
            if (fragment != null) {
                val tasks = fragment.db.taskDao().getByLecture(fragment.lectureId)
                Handler(Looper.getMainLooper()).post {
                    if (fragment.isResumed) fragment.setData(tasks)
                }
            }
        }
    }
}