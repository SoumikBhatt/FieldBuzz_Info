package com.soumik.fieldbuzz.data.repositories

import android.util.Log
import com.google.gson.Gson
import com.soumik.fieldbuzz.data.models.LoginResponse
import com.soumik.fieldbuzz.network.ErrorUtils
import com.soumik.fieldbuzz.network.RetrofitClient
import com.soumik.fieldbuzz.utils.FAILURE_MESSAGE
import com.soumik.fieldbuzz.utils.Resource
import java.io.IOException

class AuthenticationRepository {

    companion object {
        private const val TAG = "AUTHENTICATION"
    }

    suspend fun login(userName: String, password: String): Resource<LoginResponse> {

       return try {
           val response = RetrofitClient.webService.login(userName, password)

           when(response.code()) {
               200 -> {
                   if (response.body()!=null) {
                       if (response.body()!!.success) Resource.success(response.body()!!)
                       else Resource.error(response.body()!!.message)
                   } else {
                       Log.e(TAG, "login: Response body is null")
                       Resource.error(FAILURE_MESSAGE)
                   }
               }
               400 -> {
                   val error = ErrorUtils.parseError(response)
                   Log.e(TAG, "login: ${error.message}")

                   Resource.error(error.message)
               }
               else -> Resource.error(FAILURE_MESSAGE)
           }

        } catch (t: Throwable) {
           Log.e(TAG, "login: Exception: ${t.localizedMessage}")
            when (t) {
                is IOException -> Resource.error("Network Failure")
                else -> Resource.error(FAILURE_MESSAGE)
            }
        }
    }
}