package com.bendezu.tinkofffintech.network.models

import com.google.gson.annotations.SerializedName

class ConnectionsResponse (
    @SerializedName("courses") val courses: List<Course>
)

class Course (
    @SerializedName("title") val  title: String,
    @SerializedName("url") val url: String
)