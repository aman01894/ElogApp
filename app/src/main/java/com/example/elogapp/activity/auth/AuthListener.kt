package com.example.elogapp.activity.auth

import com.example.elogapp.repository.responses.login.AuthResponse
import com.example.elogapp.repository.responses.user.UserResponse

interface AuthListener {

    fun onStarted()
    suspend fun onSuccess(user: AuthResponse)
    fun onSuccess(user: UserResponse)
    fun onSuccess()
    fun onFailure(message : String)
}