package com.bendezu.tinkofffintech.network

import com.google.gson.annotations.SerializedName

class User (
    val email: String,

    @SerializedName("first_name")
    val firstname: String,

    @SerializedName("last_name")
    val lastname: String,

    @SerializedName("middle_name")
    val middlename: String? = null,

    val avatar: String? = null
)

class UserCredential (
    val email: String,
    val password: String
)

class UserResponse (
    val user: User,
    val status: String
)