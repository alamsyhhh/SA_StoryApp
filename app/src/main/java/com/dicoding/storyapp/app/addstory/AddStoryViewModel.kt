package com.dicoding.storyapp.app.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.core.data.CoreRepository
import com.dicoding.storyapp.core.data.Result
import com.dicoding.storyapp.core.data.remote.response.GeneralResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repositoryMain: CoreRepository) : ViewModel()  {
    private val resultLiveData = MutableLiveData<Result<GeneralResponse>>()

    fun addNewStory(
        token: String,
        image: MultipartBody.Part,
        desc: RequestBody
    ): LiveData<Result<GeneralResponse>> {
        viewModelScope.launch {
            val result = repositoryMain.addNewStory(token, image, desc)
            resultLiveData.value = result
        }
        return resultLiveData
    }
}