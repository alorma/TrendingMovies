package com.alorma.myapplication.infrastructure.network

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url()
                .newBuilder()
                .addQueryParameter(NetworkConfig.API_KEY_PARAM, NetworkConfig.API_TOKEN)
                .build()
        val newRequest = request.newBuilder()
                .url(url)
                .build()

        return chain.proceed(newRequest)
    }
}