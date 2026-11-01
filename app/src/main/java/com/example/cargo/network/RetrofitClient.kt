package com.example.cargo.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter-gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // TODO: Kargo backend sunucunuzun temel URL adresini buraya yazmalısınız
    private const val BASE_URL = "https://cargomanagement.com" 

    private val okHttpClient: OkHttpClient by lazy {
        // Loglama Arayüzü: Gönderilen ve gelen tüm API isteklerini Logcat üzerinde görmenizi sağlar
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Sunucuya bağlanma süresi sınırı
            .readTimeout(30, TimeUnit.SECONDS)    // Veri okuma süresi sınırı
            .writeTimeout(30, TimeUnit.SECONDS)   // Veri yazma (konum gönderme vb.) süresi sınırı
            .build()
    }

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // JSON verilerini Kotlin modellerine dönüştürür
            .build()
            .create(ApiService::class.java)
    }
}
