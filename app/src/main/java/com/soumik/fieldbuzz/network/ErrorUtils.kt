package com.soumik.fieldbuzz.network

import com.soumik.fieldbuzz.data.models.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException


/**
 * Created by Soumik on 10,December,2020
 * ITmedicus,
 * Dhaka, Bangladesh.
 */
object ErrorUtils {
    fun parseError(response: Response<*>): LoginResponse {
        val converter: Converter<ResponseBody, LoginResponse> = RetrofitClient.retrofit
            .responseBodyConverter(LoginResponse::class.java, arrayOfNulls<Annotation>(0))
        val error: LoginResponse
        error = try {
            converter.convert(response.errorBody()!!)!!
        } catch (e: IOException) {
            return LoginResponse(false,null,null)
        }
        return error
    }
}