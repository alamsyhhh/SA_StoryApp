package com.dicoding.storyapp.app.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.core.data.CoreRepository
import com.dicoding.storyapp.core.data.local.paging.StoryEntity

class MainViewModel(private val repositoryMain: CoreRepository) : ViewModel() {

    private val token = MutableLiveData<String?>()

    fun getStories(token: String): LiveData<PagingData<StoryEntity>> = repositoryMain.getAllStories(token).cachedIn(viewModelScope)

    fun getPreference(context: Context): LiveData<String?> {
        val tokenData = repositoryMain.getPreference(context)
        token.value = tokenData
        return token
    }

    fun setPreference(token: String, context: Context) =
        repositoryMain.savePreference(token, context)
}