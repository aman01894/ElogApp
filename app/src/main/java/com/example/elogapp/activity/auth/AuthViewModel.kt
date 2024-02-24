package com.example.elogapp.activity.auth

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.elogapp.database.UserDetails
import com.example.elogapp.repository.UserRepository
import com.example.elogapp.util.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    var email: String? = null
    var password: String? = null
    var checkedValue: Boolean = false
    var authListener: AuthListener? = null


    fun getLoggedInUser() = repository.getUser()

    fun onClickLoginButton(view: View) {

        authListener?.onStarted()
        if (email.isNullOrEmpty()) {
            authListener?.onFailure("User ID is required")
            return
        }
        if (password.isNullOrEmpty()) {
            authListener?.onFailure("Password is required")
            return
        }

        Coroutines.main {
            try {

                val eventObj = JSONObject()
                eventObj.put("userName", email)
                eventObj.put("password", password)
                eventObj.put("role", AppConstants.USER_ROLE)

                val requestBody =  eventObj.toString().toRequestBody()

                //Log.d("Login Request=>", "$requestBody")

                val authResponse = repository.userLogin(requestBody)
                AppUtils.logger("Response: $authResponse")
                authResponse?.let {
                    authListener?.onSuccess(authResponse)
                    return@main
                }
                authListener?.onFailure("not saved")

            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)

            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }

        }
    }


    /**
     * Save User Details
     */
    suspend fun saveUserDetails(userDetails: UserDetails) {

        repository.deleteUser(userDetails.id)
        repository.saveUser(userDetails)

    }


}