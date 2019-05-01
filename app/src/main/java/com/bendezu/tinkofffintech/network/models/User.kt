package com.bendezu.tinkofffintech.network.models

import com.google.gson.annotations.SerializedName

class User (
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstname: String,
    @SerializedName("last_name") val lastname: String,
    @SerializedName("middle_name") val middlename: String? = null,
    @SerializedName("birthday") val birthday: String? = null,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("phone_mobile") val phoneNumber: String? = null,
    @SerializedName("region") val region: String? = null,
    @SerializedName("school") val school: String? = null,
    @SerializedName("school_graduation") val schoolGraduation: String? = null,
    @SerializedName("university") val university: String? = null,
    @SerializedName("faculty") val faculty: String? = null,
    @SerializedName("university_graduation") val universityGraduation: String? = null,
    @SerializedName("department") val department: String? = null,
    @SerializedName("current_work") val currentWork: String? = null
)

class UserResponse (
    @SerializedName("user") val user: User,
    @SerializedName("status") val status: String
)