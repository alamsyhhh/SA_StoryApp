package com.dicoding.storyapp.app.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.core.data.CoreRepository

class MainViewModel(private val repositoryMain: CoreRepository) : ViewModel() {

    private val token = MutableLiveData<String?>()

    fun getStories(token: String) = repositoryMain.getAllStories(token)

    fun getPreference(context: Context): LiveData<String?> {
        val tokenData = repositoryMain.getPreference(context)
        token.value = tokenData
        return token
    }

    fun setPreference(token: String, context: Context) =
        repositoryMain.savePreference(token, context)
}