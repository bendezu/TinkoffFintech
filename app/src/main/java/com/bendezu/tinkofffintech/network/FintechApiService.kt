package com.bendezu.tinkofffintech.network

import com.bendezu.tinkofffintech.network.models.*
import io.reactivex.Single
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
    fun getUser(@Header("Cookie") cookie: String) : Call<UserResponse>

    @GET("api/user")
    fun getUserRx(@Header("Cookie") cookie: String) : Single<UserResponse>

    @GET("api/course/android_spring_2019/homeworks")
    fun getHomeworks(@Header("Cookie") cookie: String): Call<HomeworksResponse>

    @GET("api/course/android_spring_2019/grades")
    fun getGrades(@Header("Cookie") cookie: String): Call<List<GradesResponse>>

    @GET("api/course/android_spring_2019/grades")
    fun getGradesRx(@Header("Cookie") cookie: String): Single<List<GradesResponse>>
}