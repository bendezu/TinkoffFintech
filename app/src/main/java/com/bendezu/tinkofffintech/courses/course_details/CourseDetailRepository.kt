package com.bendezu.tinkofffintech.courses.course_details

import android.content.SharedPreferences
import android.util.Base64
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.getCookie
import com.bendezu.tinkofffintech.getCourse
import com.bendezu.tinkofffintech.getCourseDetails
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.models.CourseDetails
import com.bendezu.tinkofffintech.saveCourseDetails
import io.reactivex.Single
import javax.inject.Inject

@ActivityScope
class CourseDetailRepository @Inject constructor(private val preferences: SharedPreferences,
                                                 private val apiService: FintechApiService) {

    fun getData(): Single<CourseDetails> {
        val cookie = preferences.getCookie()
        val courseUrl = preferences.getCourse().url
        return apiService.getCourseDetailsRx(cookie, courseUrl)
            .map {
                val decoded = decodeHtml(it.html)
                val details = CourseDetails(it.title, decoded)
                preferences.saveCourseDetails(details)
                return@map details
            }
    }

    fun getSavedData() = preferences.getCourseDetails()

    private fun decodeHtml(html: String): String {
        val unencodedHtml = html
            .replace("\\\"", "\"")
            .replace("\\n","")
            .replace("100%", "0%")
        return Base64.encodeToString(unencodedHtml.toByteArray(), Base64.NO_PADDING)
    }

}