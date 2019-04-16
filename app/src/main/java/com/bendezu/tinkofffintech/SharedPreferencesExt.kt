package com.bendezu.tinkofffintech

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.bendezu.tinkofffintech.network.User

private const val TAG = "Preferences"

private const val PREF_FIRST_NAME = "first_name"
private const val PREF_SECOND_NAME = "second_name"
private const val PREF_PATRONYMIC = "patronymic"
private const val PREF_AVATAR = "avatar"
private const val PREF_COOKIE = "cookie"
private const val PREF_EXPIRES = "expires"
private const val PREF_RECENT_STUDENTS_UPDATE = "students_update"

fun SharedPreferences.saveUser(user: User) {
    this.edit {
        putString(PREF_FIRST_NAME, user.firstname)
        putString(PREF_SECOND_NAME, user.lastname)
        putString(PREF_PATRONYMIC, user.middlename)
        if (user.avatar != null) putString(PREF_AVATAR, user.avatar)
    }
}
fun SharedPreferences.getFirstName() = this.getString(PREF_FIRST_NAME, "").orEmpty()
fun SharedPreferences.getSecondName() = this.getString(PREF_SECOND_NAME, "").orEmpty()
fun SharedPreferences.getPatronymic() = this.getString(PREF_PATRONYMIC, "").orEmpty()
fun SharedPreferences.getAvatar() = this.getString(PREF_AVATAR, null)


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