package com.bendezu.tinkofffintech.network

import com.bendezu.tinkofffintech.network.models.*
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface FintechApiService {

    companion object {
        const val BASE_URL_CONCAT = "https://fintech.tinkoff.ru"
        const val BASE_URL = "$BASE_URL_CONCAT/"
    }

    @POST("api/signin")
    fun signIn(@Body credentials: UserCredential) : Call<User>

    @GET("api/user")
    fun getUser(@Header("Cookie") cookie: String) : Call<UserResponse>

    @GET("api/user")
    fun getUserRx(@Header("Cookie") cookie: String) : Single<UserResponse>

    @GET("api/course/{course}/homeworks")
    fun getHomeworks(@Header("Cookie") cookie: String,
                     @Path("course") course: String): Call<HomeworksResponse>

    @GET("api/course/{course}/homeworks")
    fun getHomeworksRx(@Header("Cookie") cookie: String,
                       @Path("course") course: String): Single<HomeworksResponse>

    @GET("api/course/{course}/grades")
    fun getGradesRx(@Header("Cookie") cookie: String,
                    @Path("course") course: String): Single<List<GradesResponse>>

    @GET("api/calendar/list/event")
    fun getEventsRx(): Single<EventsResponse>

    @GET("api/connections")
    fun getConnectionsRx(@Header("Cookie") cookie: String): Single<ConnectionsResponse>

    @GET("https://fintech.tinkoff.ru/api/course/{course}/about")
    fun getCourseDetailsRx(@Header("Cookie") cookie: String,
                           @Path("course") course: String): Single<CourseDetails>
}