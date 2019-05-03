package com.bendezu.tinkofffintech.courses

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.data.dao.LectureDao
import com.bendezu.tinkofffintech.data.dao.StudentDao
import com.bendezu.tinkofffintech.data.dao.TaskDao
import com.bendezu.tinkofffintech.data.entity.LectureEntity
import com.bendezu.tinkofffintech.data.entity.StudentEntity
import com.bendezu.tinkofffintech.data.entity.TaskEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.getCookie
import com.bendezu.tinkofffintech.getCourse
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.models.Course
import com.bendezu.tinkofffintech.network.models.toEntities
import com.bendezu.tinkofffintech.network.models.toEntity
import com.bendezu.tinkofffintech.saveCourse
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import javax.inject.Inject

class CoursesData(
    val course: Course,
    val students: List<StudentEntity>,
    val tasks: List<TaskEntity>,
    val lectures: List<LectureEntity>
)

@ActivityScope
class CoursesRepository @Inject constructor(private val apiService: FintechApiService,
                                            private val sharedPreferences: SharedPreferences,
                                            private val taskDao: TaskDao,
                                            private val lectureDao: LectureDao,
                                            private val studentDao: StudentDao
) {

    fun getData(): Flowable<CoursesData> {

        val databaseSource = Singles.zip(
            Single.fromCallable { sharedPreferences.getCourse() },
            studentDao.getAllRx(),
            taskDao.getAllRx(),
            lectureDao.getAllRx()
        )
        { course, students, tasks, lectures ->
            CoursesData(course, students, tasks, lectures)
        }

        val networkSource=
            Single.fromCallable { sharedPreferences.getCookie() }.flatMap { cookie ->
                getCourseSingle(cookie).flatMap { course ->
                    Singles.zip(
                        getStudentsSingle(cookie, course),
                        getHomeworksSingle(cookie, course)
                    )
                    { students, homeworks ->
                        CoursesData(course, students, homeworks.first, homeworks.second)
                    }
                }
            }

        return databaseSource.concatWith(networkSource)
    }

    private fun getCourseSingle(cookie: String) =
        apiService.getConnectionsRx(cookie).map {
            sharedPreferences.saveCourse(it.courses[0])
            return@map it.courses[0]
        }

    private fun getStudentsSingle(cookie: String, course: Course) =
        apiService.getGradesRx(cookie, course.url).map { response ->
            val students = response[1].toEntity().sortedBy { it.id }
            studentDao.updateData(students)
            return@map students
        }

    private fun getHomeworksSingle(cookie: String, course: Course) =
        apiService.getHomeworksRx(cookie, course.url).map { response ->
            val (lectures, tasks) = response.toEntities()
            lectureDao.updateData(lectures)
            taskDao.insertAll(tasks)
            return@map tasks to lectures
        }
}