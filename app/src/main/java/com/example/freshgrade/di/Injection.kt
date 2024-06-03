package com.example.freshgrade.di

import android.content.Context
import com.example.freshgrade.data.api.ApiConfig
import com.example.freshgrade.data.pref.UserPreference
import com.example.freshgrade.data.pref.dataStore
import com.example.freshgrade.data.repo.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService,pref)
    }
}