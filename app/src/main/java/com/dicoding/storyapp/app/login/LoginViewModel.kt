package com.dicoding.storyapp.app.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.core.data.CoreRepository

class LoginViewModel(private val repositoryMain: CoreRepository) : ViewModel()  {

    private val token = MutableLiveData<String?>()

    fun login(email: String, password: String) = repositoryMain.loginUser(email, password)

    fun setPreference(token: String, context: Context) =
        repositoryMain.savePreference(token, context)

    fun getPreference(context: Context): LiveData<String?> {
        val tokenData = repositoryMain.getPreference(context)
        token.value = tokenData
        return token
    }
}