package com.dicoding.storyapp.app

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.app.addstory.AddStoryViewModel
import com.dicoding.storyapp.app.details.DetailsViewModel
import com.dicoding.storyapp.app.home.MainViewModel
import com.dicoding.storyapp.app.login.LoginViewModel
import com.dicoding.storyapp.app.signup.SignUpViewModel
import com.dicoding.storyapp.core.data.CoreRepository
import com.dicoding.storyapp.core.di.Injection

class ViewModelFactory private constructor(private val repositoryMain: CoreRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repositoryMain) as T
        }else if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(repositoryMain) as T
        }else if(modelClass.isAssignableFrom(SignUpViewModel::class.java)){
            return SignUpViewModel(repositoryMain) as T
        }else if(modelClass.isAssignableFrom(AddStoryViewModel::class.java)){
            return AddStoryViewModel(repositoryMain) as T
        }else if(modelClass.isAssignableFrom(DetailsViewModel::class.java)){
            return DetailsViewModel(repositoryMain) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}