package com.dicoding.storyapp.app.details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.core.data.CoreRepository

class DetailsViewModel(private val repositoryMain: CoreRepository) : ViewModel()  {

    private val token = MutableLiveData<String?>()

    fun getPreference(context: Context): LiveData<String?> {
        val tokenData = repositoryMain.getPreference(context)
        token.value = tokenData
        return token
    }

    fun storydetail(token: String, id: String) = repositoryMain.getStoryDetail(token,id)
}