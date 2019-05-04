package com.bendezu.tinkofffintech.network.models

import com.google.gson.annotations.SerializedName

class CourseDetails (
    @SerializedName("title") val title: String,
    @SerializedName("html") val html: String
)