package com.example.freshgrade.data.api

import android.content.Context
import com.example.freshgrade.data.pref.UserModel
import com.example.freshgrade.data.pref.UserPreference
import com.example.freshgrade.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//object ApiConfig {
//
//    const val BASE_URL = "https://freshgrade-backend-bcpt44l36a-et.a.run.app/"
//
//
//    fun getApiService(): ApiService {
//        val loggingInterceptor = HttpLoggingInterceptor()
//            .setLevel(HttpLoggingInterceptor.Level.BODY)
//        val client: OkHttpClient = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//        return retrofit.create(ApiService::class.java)
//    }
//}

//object ApiConfig {
//
//    const val BASE_URL = "https://freshgrade-backend-bcpt44l36a-et.a.run.app/"
//
//    fun getApiService(): ApiService {
//        val loggingInterceptor = HttpLoggingInterceptor()
//            .setLevel(HttpLoggingInterceptor.Level.BODY)
//
//        val authInterceptor = Interceptor { chain ->
//            val original = chain.request()
//            val requestBuilder = original.newBuilder()
//                .header("Authorization", "Bearer $it")
//            val request = requestBuilder.build()
//            chain.proceed(request)
//        }
//
//        val client: OkHttpClient = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .addInterceptor(authInterceptor)
//            .build()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//
//        return retrofit.create(ApiService::class.java)
//    }
//}

object ApiConfig {

    const val BASE_URL = "https://freshgrade-backend-bcpt44l36a-et.a.run.app/"

    fun getApiService(context: Context): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val token = runBlocking {
                UserPreference.getInstance(context.dataStore).getToken().first()
            }

            val requestBuilder = original.newBuilder()
            token?.let {
                requestBuilder.header("Authorization", "Bearer $it")
            }
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}