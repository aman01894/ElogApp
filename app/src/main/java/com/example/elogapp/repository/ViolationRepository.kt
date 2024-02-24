package com.example.elogapp.repository


import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.database.Violation
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.network.SafeApiRequest
import com.example.elogapp.repository.responses.login.AuthResponse
import okhttp3.RequestBody

class ViolationRepository(
    private val api: MyApi,
    private val db: MyRoomDb
) : SafeApiRequest() {


    suspend fun saveViolation(requestBody: RequestBody): AuthResponse {

        return apiRequest {
            api.userSignIn(requestBody)

        }

    }


    suspend fun insertViolation(violation: Violation) = db.getViolationDao().insertViolation(violation)

    suspend fun updateViolation(violation: Violation) = db.getViolationDao().update(violation)

    suspend fun selectViolation(violation: Violation) = db.getViolationDao().getViolationList()

    /**
     * Delete User From DB
     */
    suspend fun deleteViolation(violation: Violation) = db.getViolationDao().delete(violation)




}