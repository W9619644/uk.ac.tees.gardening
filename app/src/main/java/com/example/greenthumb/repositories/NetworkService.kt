package com.example.greenthumb.repositories

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object NetworkService {

    private const val BASE_URL = "https://trefle.io"
    private const val BASE_URL2 = "https://newsapi.org"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .build()

  private val retrofit2 = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_URL2)
        .build()

    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val apiService2 : ApiService by lazy {
        retrofit2.create(ApiService::class.java)
    }

}