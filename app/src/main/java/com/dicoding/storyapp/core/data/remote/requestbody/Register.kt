package com.dicoding.storyapp.core.data.remote.requestbody

data class Register(
    val name: String,
    val email: String,
    val password: String
)