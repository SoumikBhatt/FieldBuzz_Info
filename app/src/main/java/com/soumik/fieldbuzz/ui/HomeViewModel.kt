package com.soumik.fieldbuzz.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soumik.fieldbuzz.data.models.DetailsResponse
import com.soumik.fieldbuzz.data.models.FileUploadResponse
import com.soumik.fieldbuzz.data.repositories.HomeRepository
import com.soumik.fieldbuzz.utils.*

class HomeViewModel:ViewModel() {

    companion object {
        private const val TAG = "HOME_VIEW_MODEL"
    }

    private val mHomeRepository = HomeRepository()

    val infoLiveData = MutableLiveData<Resource<DetailsResponse>> ()


    suspend fun submitInformation(name: String, email: String, phone: String, address: String?, university: String, gradYear: Int, cgpa: Double?, experience: Int?, workPlace: String?,
        applyingOn: String, salary: Int, reference: String?, projectUrl: String, selectedPDF: Uri?, inputToken: String, fileToken: String, updateTime: Long?, createTime: Long,pdfUri:Uri?) {

        infoLiveData.postValue(Resource.loading("Please wait, your information submission on progress..."))

        if (hasInternetConnection()) {
            val resource = mHomeRepository.submitInformation(name, email, phone, address, university, gradYear, cgpa, experience,
                workPlace, applyingOn, salary, reference, projectUrl, selectedPDF, inputToken, fileToken, updateTime, createTime)

            when(resource.status) {
                Status.SUCCESS ->{
                    Log.d(TAG, "submitInformation: Info submitted")
                    SessionManager.lastInputToken = resource.data?.tsyncId

                    infoLiveData.postValue(Resource.loading("Please wait, your CV file upload is on progress..."))
                    infoLiveData.postValue(fileUpload(pdfUri,resource.data?.cvFile?.id,resource))
                }
                Status.ERROR ->{
                    infoLiveData.postValue(Resource.error(resource.error))
                }
                Status.LOADING->{
                    infoLiveData.postValue(Resource.loading("Please wait, your information submission on progress..."))
                }
            }

        } else infoLiveData.postValue(Resource.error("There is no Internet connection"))
    }

    private suspend fun fileUpload(pdfUri: Uri?, fileToken: Int?,data:Resource<DetailsResponse>): Resource<DetailsResponse>? {


        if (hasInternetConnection()) {
            return try {

                Resource.loading<DetailsResponse>("Please wait, your CV file upload is on progress...")

                val resource = mHomeRepository.fileUpload(fileToken,pdfUri)

                when(resource.status) {
                    Status.SUCCESS ->{
                        Log.d(TAG, "fileUpload: CV Upload Success!!")
                        SessionManager.lastFileToken = resource.data?.tsyncId
                        return Resource.success(data.data!!)
                    }
                    Status.ERROR ->{
                        Resource.error(resource.error)
                    }
                    Status.LOADING->{
                        Resource.loading("Please wait, your CV file upload is on progress...")
                    }
                }

            } catch (e:Exception) {
                Log.e(TAG, "fileUpload: Exception: ${e.printStackTrace()}")
                Resource.error(FAILURE_MESSAGE)
            }

        } else return Resource.error("There is no Internet connection")
    }

}