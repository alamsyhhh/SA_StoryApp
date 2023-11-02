package com.dicoding.storyapp.core.di

import android.content.Context
import com.dicoding.storyapp.core.data.CoreRepository
import com.dicoding.storyapp.core.data.local.paging.StoryDatabase
import com.dicoding.storyapp.core.data.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context): CoreRepository {
        val apiService = ApiConfig.getApiService(context)
        val database= StoryDatabase.getInstance(context)
        return CoreRepository.getInstance(apiService,database)
    }
}