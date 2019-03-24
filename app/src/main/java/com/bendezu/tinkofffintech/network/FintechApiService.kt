package com.bendezu.tinkofffintech.network

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface FintechApiService {

    companion object {
        const val BASE_URL = "https://fintech.tinkoff.ru/"
    }

    @POST("api/signin")
    fun signIn(@Body credentials: UserCredential) : Call<User>

    @GET("api/user")
    fun getUser(@Header("Cookie") cookie: String) : Call<User>
}

class User (
    val email: String,

    @SerializedName("first_name")
    val firstname: String,

    @SerializedName("last_name")
    val lastname: String,

    @SerializedName("middle_name")
    val middlename: String?,

    val avatar: String?
)

class UserCredential (
    val email: String,
    val password: String
)