package com.example.elogapp.repository.responses.login

import com.example.elogapp.database.UserDetails
import com.google.gson.annotations.SerializedName


data class AuthResponse(

        @SerializedName("status")
        val status: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("exceptionMessage")
        val exceptionMessage: String,
        @SerializedName("data")
        val data: UserDetails
)
