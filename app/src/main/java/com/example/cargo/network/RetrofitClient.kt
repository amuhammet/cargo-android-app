package com.example.cargo.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // TODO: Kendi yerel veya uzak sunucu adresinizle değiştirin. 
    // Emülatör kullanıyorsanız localhost için "http://10.0.2" adresini tercih edebilirsiniz.
    private const val BASE_URL = "https://cargo-app.com" 

    /**
     * Ağ isteklerinin zaman aşımı sürelerini ayarlayan ve loglama yapan HTTP istemcisi
     */
    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // BODY seviyesi tüm istek ve yanıt detaylarını (Header, Body) Logcat'e basar
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS) // Sunucuya bağlanma sınırı
            .readTimeout(15, TimeUnit.SECONDS)    // Veri okuma sınırı
            .writeTimeout(15, TimeUnit.SECONDS)   // Veri yazma sınırı
            .build()
    }

    /**
     * Retrofit nesnesinin ilklendirilmesi
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // JSON serileştirme için GSON desteği
            .build()
    }

    /**
     * Proje genelinde (Repository'lerde) çağrılacak olan hazır servis nesnesi
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
