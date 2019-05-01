package com.bendezu.tinkofffintech.network.models

import com.google.gson.annotations.SerializedName

class UserCredential (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)