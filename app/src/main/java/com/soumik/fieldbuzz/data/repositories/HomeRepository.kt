package com.soumik.fieldbuzz.data.repositories

import android.net.Uri
import android.util.Log
import com.soumik.fieldbuzz.FieldBuzz
import com.soumik.fieldbuzz.data.models.DetailsResponse
import com.soumik.fieldbuzz.data.models.FileUploadResponse
import com.soumik.fieldbuzz.network.ErrorUtils
import com.soumik.fieldbuzz.network.RetrofitClient
import com.soumik.fieldbuzz.utils.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

class HomeRepository {

    companion object {
        private const val TAG = "HOME_REPOSITORY"
    }

    suspend fun submitInformation(
        name: String,
        email: String,
        phone: String,
        address: String?,
        university: String,
        gradYear: Int,
        cgpa: Double?,
        experience: Int?,
        workPlace: String?,
        applyingOn: String,
        salary: Int,
        reference: String?,
        projectUrl: String,
        selectedPDF: Uri?,
        inputToken: String,
        fileToken: String,
        updateTime: Long?,
        createTime: Long
    ):Resource<DetailsResponse> {

        return try {
            val response = RetrofitClient.webService.recruitmentInformation("Token ${SessionManager.token!!}",inputToken,name,email,phone,address,university,
                gradYear,cgpa,experience,workPlace,applyingOn,salary,reference,projectUrl,fileToken,updateTime?.toInt(),createTime.toInt())

            when(response.code()) {
                201 -> {
                    if (response.body()!=null) {
                        if (response.body()!!.success) Resource.success(response.body()!!)
                        else Resource.error(response.body()!!.message)
                    } else {
                        Log.e(TAG, "submitInformation: Response body is null")
                        Resource.error(FAILURE_MESSAGE)
                    }
                }
                400 -> {
                    val error = ErrorUtils.parseError(response)
                    Log.e(TAG, "submitInformation: ${error.message}")

                    Resource.error(error.message)
                }
                else -> Resource.error(FAILURE_MESSAGE)
            }

        } catch (t: Throwable) {
            Log.e(TAG, "submitInformation: Exception: ${t.localizedMessage}")
            when (t) {
                is IOException -> Resource.error("Network Failure")
                else -> Resource.error(FAILURE_MESSAGE)
            }
        }
    }

    suspend fun fileUpload(fileToken: Int?,fileUri: Uri?):Resource<FileUploadResponse> {
        return try {
            val filePath = FileUtils.getPath(fileUri!!)
            val file = File(filePath!!)


            val fileBody = RequestBody.create(MediaType.parse("application/pdf"),file)
            val filePart = MultipartBody.Part.createFormData("file",file.name,fileBody)

            val response = RetrofitClient.webService.fileUpload("${BASE_URL}file-object/$fileToken/","Token ${SessionManager.token!!}",filePart)

            when(response.code()) {
                200 -> {
                    if (response.body()!=null) {
                        if (response.body()!!.success) Resource.success(response.body()!!)
                        else Resource.error(response.body()!!.message)
                    } else {
                        Log.e(TAG, "fileUpload: Response body is null")
                        Resource.error(FAILURE_MESSAGE)
                    }
                }
                400 -> {
                    val error = ErrorUtils.parseError(response)
                    Log.e(TAG, "fileUpload: ${error.message}")

                    Resource.error(error.message)
                }
                else -> Resource.error(FAILURE_MESSAGE)
            }

        } catch (t: Throwable) {
            t.printStackTrace()
            Log.e(TAG, "fileUpload: Exception: ${t.localizedMessage}")
            when (t) {
                is IOException -> Resource.error("File not found! Try again")
                else -> Resource.error(FAILURE_MESSAGE)
            }
        }
    }
}