package com.alorma.myapplication.infrastructure.di

import com.alorma.data.net.MovieApi
import com.alorma.data.net.config.ConfigApi
import com.alorma.myapplication.infrastructure.network.NetworkConfig
import com.alorma.myapplication.infrastructure.network.TokenInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory<ConfigApi> { get<Retrofit>().create() }
    factory<MovieApi> { get<Retrofit>().create() }

    factory {
        Retrofit.Builder()
                .apply {
                    addCallAdapterFactory(CoroutineCallAdapterFactory())
                    addConverterFactory(GsonConverterFactory.create())
                    baseUrl(NetworkConfig.API_URL)
                    client(get())
                }
                .build()
    }

    factory {
        OkHttpClient.Builder()
                .addInterceptor(get<TokenInterceptor>())
                .addInterceptor(get<ChuckInterceptor>())
                .build()
    }

    factory { TokenInterceptor() }
    factory { ChuckInterceptor(androidContext()) }
}