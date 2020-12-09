package com.soumik.fieldbuzz.network

import com.soumik.fieldbuzz.data.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface WebService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") userName:String,
        @Field("password") password:String
    ):Response<LoginResponse>
}