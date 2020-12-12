package com.soumik.fieldbuzz.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soumik.fieldbuzz.data.models.LoginResponse
import com.soumik.fieldbuzz.data.repositories.AuthenticationRepository
import com.soumik.fieldbuzz.utils.Resource
import com.soumik.fieldbuzz.utils.SessionManager
import com.soumik.fieldbuzz.utils.Status
import com.soumik.fieldbuzz.utils.hasInternetConnection

class LoginViewModel:ViewModel() {
    companion object {
        private const val TAG = "LOGIN_VIEW_MODEL"
    }

    private val mRepository = AuthenticationRepository()

    val loginLiveData = MutableLiveData<Resource<LoginResponse>>()

    suspend fun login(username:String,password:String) {

        loginLiveData.postValue(Resource.loading(null))

        if (hasInternetConnection()) {
            val resource = mRepository.login(username,password)
            when(resource.status) {
                Status.SUCCESS -> {
                    Log.d(TAG, "login: Logged In Successfully!")
                    SessionManager.isLoggedIn=true
                    SessionManager.token = resource.data?.token

                    loginLiveData.postValue(Resource.success(resource.data!!))
                }
                Status.LOADING ->{
                    loginLiveData.postValue(Resource.loading(null))
                }

                Status.ERROR -> {
                    loginLiveData.postValue(Resource.error(resource.error))
                }
            }
        } else loginLiveData.postValue(Resource.error("There is no Internet connection"))

    }
}