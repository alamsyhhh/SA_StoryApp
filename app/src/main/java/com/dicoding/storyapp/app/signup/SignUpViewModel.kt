package com.dicoding.storyapp.app.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.core.data.CoreRepository
import com.dicoding.storyapp.core.data.Result
import com.dicoding.storyapp.core.data.remote.response.GeneralResponse
import kotlinx.coroutines.launch

class SignUpViewModel(private val repositoryMain: CoreRepository) : ViewModel()  {

    private val resultLiveData = MutableLiveData<Result<GeneralResponse>>()

    fun register(
        name: String,
        password: String,
        email: String
    ): LiveData<Result<GeneralResponse>> {
        viewModelScope.launch {
            val result = repositoryMain.registerUser(name, password, email)
            resultLiveData.value = result
        }
        return resultLiveData
    }
}