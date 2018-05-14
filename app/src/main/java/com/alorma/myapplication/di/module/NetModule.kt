package com.alorma.myapplication.di.module

import android.content.Context
import com.alorma.myapplication.data.net.config.ConfigApi
import com.alorma.myapplication.data.net.MovieApi
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetModule(private val context: Context) {

    companion object {
        const val API_URL = "https://api.themoviedb.org/3/"
        const val API_KEY_PARAM = "api_key"
        const val API_TOKEN = "138c49c93ee96a29add33a767c3ae27a"

        const val TOKEN_INTERCEPTOR = "TOKEN_INTERCEPTOR"
        const val CHUCK_INTERCEPTOR = "CHUCK_INTERCEPTOR"
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .apply {
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                addConverterFactory(GsonConverterFactory.create())
                baseUrl(API_URL)
                client(okHttpClient)
            }
            .build()

    @Provides
    @Singleton
    fun providesOkHttpClient(@Named(TOKEN_INTERCEPTOR) interceptor: Interceptor,
                             @Named(CHUCK_INTERCEPTOR) chuck: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(chuck)
            .build()

    @Provides
    @Singleton
    @Named(TOKEN_INTERCEPTOR)
    fun provideTokenInterceptor(): Interceptor = Interceptor {
        val request = it.request()
        val newRequest = request.newBuilder()
                .url(request.url().newBuilder().addQueryParameter(API_KEY_PARAM, API_TOKEN).build())
                .build()

        it.proceed(newRequest)
    }

    @Provides
    @Singleton
    @Named(CHUCK_INTERCEPTOR)
    fun provideChuckInterceptor(): Interceptor = ChuckInterceptor(context)

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): MovieApi = retrofit.create()

    @Provides
    @Singleton
    fun providesConfigApi(retrofit: Retrofit): ConfigApi = retrofit.create()
}