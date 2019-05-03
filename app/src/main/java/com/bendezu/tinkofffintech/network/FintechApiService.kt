package com.bendezu.tinkofffintech.network

import com.bendezu.tinkofffintech.network.models.*
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

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

    @GET("api/course/{course}/homeworks")
    fun getHomeworks(@Header("Cookie") cookie: String,
                     @Path("course") course: String = "android_spring_2019"): Call<HomeworksResponse>

    @GET("api/course/{course}/homeworks")
    fun getHomeworksRx(@Header("Cookie") cookie: String,
                       @Path("course") course: String = "android_spring_2019"): Single<HomeworksResponse>

    @GET("api/course/{course}/grades")
    fun getGradesRx(@Header("Cookie") cookie: String,
                    @Path("course") course: String = "android_spring_2019"): Single<List<GradesResponse>>

    @GET("api/calendar/list/event")
    fun getEventsRx(): Single<EventsResponse>

    @GET("api/connections")
    fun getConnectionsRx(@Header("Cookie") cookie: String): Single<ConnectionsResponse>
}