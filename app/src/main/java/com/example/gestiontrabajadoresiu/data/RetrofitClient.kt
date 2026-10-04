package com.example.gestiontrabajadoresiu.data

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    //private const val BASE_URL = "http://10.0.2.2:5259/"
    private const val BASE_URL = "http://localhost:5258/"

    var authToken: String? = null
    var nombreUsuarioActual: String? = null

    private val authInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        authToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        chain.proceed(requestBuilder.build())
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val api: ApiService by lazy {
       Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}