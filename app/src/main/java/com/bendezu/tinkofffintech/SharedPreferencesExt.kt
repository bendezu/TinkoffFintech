package com.bendezu.tinkofffintech

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.bendezu.tinkofffintech.network.User

private const val TAG = "Preferences"

private const val PREF_EMAIL = "email"
private const val PREF_FIRST_NAME = "first_name"
private const val PREF_SECOND_NAME = "second_name"
private const val PREF_PATRONYMIC = "patronymic"
private const val PREF_AVATAR = "avatar"
private const val PREF_COOKIE = "cookie"
private const val PREF_EXPIRES = "expires"
private const val PREF_RECENT_STUDENTS_UPDATE = "students_update"

fun SharedPreferences.saveUser(user: User) {
    this.edit {
        putString(PREF_EMAIL, user.email)
        putString(PREF_FIRST_NAME, user.firstname)
        putString(PREF_SECOND_NAME, user.lastname)
        putString(PREF_PATRONYMIC, user.middlename)
        if (user.avatar != null) putString(PREF_AVATAR, user.avatar)
    }
}
fun SharedPreferences.getUser(): User {
    val email = this.getString(PREF_EMAIL, "").orEmpty()
    val first = this.getString(PREF_FIRST_NAME, "").orEmpty()
    val second = this.getString(PREF_SECOND_NAME, "").orEmpty()
    val middle = this.getString(PREF_PATRONYMIC, "").orEmpty()
    val avatar = this.getString(PREF_AVATAR, null)
    return User(email, first, second, middle, avatar)
}

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