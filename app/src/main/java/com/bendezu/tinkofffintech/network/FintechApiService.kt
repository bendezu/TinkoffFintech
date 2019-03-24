package com.bendezu.tinkofffintech.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface FintechApiService {

    companion object {
        const val BASE_URL = "https://fintech.tinkoff.ru/"

        fun getNewInstance() =
            Retrofit.Builder()
                .baseUrl(FintechApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create<FintechApiService>()
    }

    @POST("api/signin")
    fun signIn(@Body credentials: UserCredential) : Call<User>

    @GET("api/user")
    fun getUser(@Header("Cookie") cookie: String) : Call<UserResponse>
}