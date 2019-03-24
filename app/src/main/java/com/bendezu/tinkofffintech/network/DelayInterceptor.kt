package com.bendezu.tinkofffintech.network

import okhttp3.Interceptor
import okhttp3.Response

class DelayInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Thread.sleep(2000)
        val response = chain.proceed(request);
        return response
    }

}