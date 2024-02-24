package com.example.elogapp.repository


import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.database.UserDetails
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.network.SafeApiRequest
import com.example.elogapp.repository.responses.login.AuthResponse
import okhttp3.RequestBody

class UserRepository(
    private val api: MyApi,
    private val db: MyRoomDb
) : SafeApiRequest() {


    suspend fun userLogin(requestBody: RequestBody): AuthResponse {

        return apiRequest {
            api.userSignIn(requestBody)

        }

    }


    suspend fun saveUser(user: UserDetails) = db.getUserDao().insert(user)

    /**
     * Delete User From DB
     */
    suspend fun deleteUser(id: Int) = db.getUserDao().delete(id)

    fun getUser() = db.getUserDao().getUser()


}