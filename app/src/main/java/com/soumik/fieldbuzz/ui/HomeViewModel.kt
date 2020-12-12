package com.soumik.fieldbuzz.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soumik.fieldbuzz.data.models.DetailsResponse
import com.soumik.fieldbuzz.data.repositories.HomeRepository
import com.soumik.fieldbuzz.utils.Resource
import com.soumik.fieldbuzz.utils.Status
import com.soumik.fieldbuzz.utils.hasInternetConnection

class HomeViewModel:ViewModel() {

    companion object {
        private const val TAG = "HOME_VIEW_MODEL"
    }

    private val mHomeRepository = HomeRepository()

    val liveData = MutableLiveData<Resource<DetailsResponse>> ()

    suspend fun submitInformation(name: String, email: String, phone: String, address: String?, university: String, gradYear: Int, cgpa: Double?, experience: Int?, workPlace: String?,
        applyingOn: String, salary: Int, reference: String?, projectUrl: String, selectedPDF: Uri?, inputToken: String, fileToken: String, updateTime: Long?, createTime: Long) {

        liveData.postValue(Resource.loading("Validating..."))

        if (hasInternetConnection()) {
            val resource = mHomeRepository.submitInformation(name, email, phone, address, university, gradYear, cgpa, experience,
                workPlace, applyingOn, salary, reference, projectUrl, selectedPDF, inputToken, fileToken, updateTime, createTime)

            when(resource.status) {
                Status.SUCCESS ->{
                    Log.d(TAG, "submitInformation: Info submitted")
//                    liveData.postValue(Resource.success(resource.data!!))
                    liveData.postValue(Resource.loading("Uploading cv..."))
                }
                Status.ERROR ->{
                    liveData.postValue(Resource.error(resource.error))
                }
                Status.LOADING->{
                    liveData.postValue(Resource.loading("Submitting Information..."))
                }
            }

        } else liveData.postValue(Resource.error("There is no Internet connection"))
    }

}