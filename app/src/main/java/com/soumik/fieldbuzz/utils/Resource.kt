package com.soumik.fieldbuzz.utils

data class Resource<T> (val status: Status,val data:T?,val error:String?){

    companion object {

        fun <T> success(data:T):Resource<T> {
            return Resource(Status.SUCCESS,data,null)
        }

        fun <T> error(message:String?):Resource<T> {
            return Resource(Status.ERROR,null,message)
        }

        fun <T> loading():Resource<T> = Resource(Status.LOADING,null,null)
    }
}