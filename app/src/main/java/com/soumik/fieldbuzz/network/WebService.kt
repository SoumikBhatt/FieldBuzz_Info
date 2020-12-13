package com.soumik.fieldbuzz.network

import com.soumik.fieldbuzz.data.models.DetailsResponse
import com.soumik.fieldbuzz.data.models.FileUploadResponse
import com.soumik.fieldbuzz.data.models.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface WebService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") userName:String,
        @Field("password") password:String
    ):Response<LoginResponse>

    @FormUrlEncoded
    @POST("v0/recruiting-entities/")
    suspend fun recruitmentInformation(
            @Header("Authorization") token:String,
            @Field("tsync_id") tSyncID:String,
            @Field("name") name:String,
            @Field("email") email:String,
            @Field("phone") phone:String,
            @Field("full_address") full_address:String?,
            @Field("name_of_university") name_of_university:String,
            @Field("graduation_year") graduation_year:Int,
            @Field("cgpa") cgpa:Double?,
            @Field("experience_in_months") experience_in_months:Int?,
            @Field("current_work_place_name") current_work_place_name:String?,
            @Field("applying_in") applying_in:String,
            @Field("expected_salary") expected_salary:Int,
            @Field("field_buzz_reference") field_buzz_reference:String?,
            @Field("github_project_url") github_project_url:String,
            @Field("cv_file.tsync_id") cv_file:String,
            @Field("on_spot_update_time") on_spot_update_time:Int?,
            @Field("on_spot_creation_time") on_spot_creation_time:Int?
    ):Response<DetailsResponse>

    @Multipart
    @PUT
    suspend fun fileUpload(
        @Url url:String,
        @Header("Authorization") token:String,
//        @Part("file") file:RequestBody
        @Part file:MultipartBody.Part
    ):Response<FileUploadResponse>

}