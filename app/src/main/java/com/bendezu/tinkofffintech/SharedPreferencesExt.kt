package com.bendezu.tinkofffintech

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.bendezu.tinkofffintech.network.models.Course
import com.bendezu.tinkofffintech.network.models.CourseDetails
import com.bendezu.tinkofffintech.network.models.User

private const val TAG = "Preferences"

private const val PREF_ID = "id"
private const val PREF_EMAIL = "email"
private const val PREF_FIRST_NAME = "first_name"
private const val PREF_SECOND_NAME = "second_name"
private const val PREF_PATRONYMIC = "patronymic"
private const val PREF_BIRTHDAY = "birthday"
private const val PREF_AVATAR = "avatar"
private const val PREF_DESCRIPTION = "description"
private const val PREF_PHONE_NUMBER = "phone"
private const val PREF_REGION = "region"
private const val PREF_SCHOOL = "school"
private const val PREF_SCHOOL_GRADUATION = "school_graduation"
private const val PREF_UNIVERSITY = "university"
private const val PREF_FACULTY = "faculty"
private const val PREF_UNIVERSITY_GRADUATION = "university_graduation"
private const val PREF_DEPARTMENT = "department"
private const val PREF_CURRENT_WORK = "current_work"

private const val PREF_COURSE_TITLE = "course_name"
const val PREF_COURSE_URL = "course_url"

private const val PREF_ABOUT_TITLE = "about_title"
private const val PREF_ABOUT_HTML = "about_html"

private const val PREF_COOKIE = "cookie"
private const val PREF_EXPIRES = "expires"
private const val PREF_RECENT_STUDENTS_UPDATE = "students_update"

fun SharedPreferences.saveUser(user: User) {
    edit {
        putLong(PREF_ID, user.id)
        putString(PREF_EMAIL, user.email)
        putString(PREF_FIRST_NAME, user.firstname)
        putString(PREF_SECOND_NAME, user.lastname)
        putString(PREF_PATRONYMIC, user.middlename)
        putString(PREF_BIRTHDAY, user.birthday)
        putString(PREF_AVATAR, user.avatar)
        putString(PREF_DESCRIPTION, user.description)
        putString(PREF_PHONE_NUMBER, user.phoneNumber)
        putString(PREF_REGION, user.region)
        putString(PREF_SCHOOL, user.school)
        putString(PREF_SCHOOL_GRADUATION, user.schoolGraduation)
        putString(PREF_UNIVERSITY, user.university)
        putString(PREF_FACULTY, user.faculty)
        putString(PREF_UNIVERSITY_GRADUATION, user.universityGraduation)
        putString(PREF_DEPARTMENT, user.department)
        putString(PREF_CURRENT_WORK, user.currentWork)
    }
}

fun SharedPreferences.getUser() = User(
    id = getLong(PREF_ID, 0),
    email = getString(PREF_EMAIL, "").orEmpty(),
    firstname = getString(PREF_FIRST_NAME, "").orEmpty(),
    lastname = getString(PREF_SECOND_NAME, "").orEmpty(),
    middlename = getString(PREF_PATRONYMIC, "").orEmpty(),
    birthday = getString(PREF_BIRTHDAY, null),
    avatar = getString(PREF_AVATAR, null),
    description = getString(PREF_DESCRIPTION, null),
    phoneNumber = getString(PREF_PHONE_NUMBER, null),
    region = getString(PREF_REGION, null),
    school = getString(PREF_SCHOOL, null),
    schoolGraduation = getString(PREF_SCHOOL_GRADUATION, null),
    university = getString(PREF_UNIVERSITY, null),
    faculty = getString(PREF_FACULTY, null),
    universityGraduation = getString(PREF_UNIVERSITY_GRADUATION, null),
    department = getString(PREF_DEPARTMENT, null),
    currentWork = getString(PREF_CURRENT_WORK, null)
)

fun SharedPreferences.saveCourse(course: Course) {
    edit {
        putString(PREF_COURSE_TITLE, course.title)
        putString(PREF_COURSE_URL, course.url)
    }
}

fun SharedPreferences.getCourse() = Course(
    title = getString(PREF_COURSE_TITLE, "").orEmpty(),
    url = getString(PREF_COURSE_URL, "").orEmpty()
)

fun SharedPreferences.saveCourseDetails(course: CourseDetails) {
    edit {
        putString(PREF_ABOUT_TITLE, course.title)
        putString(PREF_ABOUT_HTML, course.html)
    }
}

fun SharedPreferences.getCourseDetails() = CourseDetails(
    title = getString(PREF_ABOUT_TITLE, "").orEmpty(),
    html = getString(PREF_ABOUT_HTML, "").orEmpty()
)

fun SharedPreferences.saveCookie(cookie: String, expires: String) {
    Log.d(TAG, "Cookie: $cookie")
    Log.d(TAG, "Expires: $expires")
    this.edit {
        putString(PREF_COOKIE, cookie)
        putString(PREF_EXPIRES, expires)
    }
}

fun SharedPreferences.getCookie() = this.getString(PREF_COOKIE, null).orEmpty()
fun SharedPreferences.getExpires() = this.getString(PREF_EXPIRES, null).orEmpty()

fun SharedPreferences.saveRecentStudentUpdate(millis: Long) {
    this.edit().putLong(PREF_RECENT_STUDENTS_UPDATE, millis).apply()
}

fun SharedPreferences.getRecentStudentUpdate() = this.getLong(PREF_RECENT_STUDENTS_UPDATE, 0)