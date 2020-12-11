package com.soumik.fieldbuzz.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.soumik.fieldbuzz.FieldBuzz

const val BASE_URL = "https://recruitment.fisdev.com/api/"
const val FAILURE_MESSAGE = "Something went wrong! Please try again later.."


fun toolbarStyle(context: AppCompatActivity, toolbar: Toolbar, title: String) {

    (context).setSupportActionBar(toolbar)
    context.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    context.supportActionBar!!.setDisplayShowHomeEnabled(true)
    context.supportActionBar!!.setDisplayShowTitleEnabled(true)

    context.supportActionBar!!.title = title
}

fun showToast(context: Context,message:String,length:Int) = Toast.makeText(context,message,length).show()

fun showSnackBar(parent:View,message: String,action:String,length: Int) {
    Snackbar.make(parent,message,length).apply {
        setAction(action) {
            this.dismiss()
        }.show()
    }
}

fun lightStatusBar(activity: Activity, value:Boolean){
    if (value){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}

fun hasInternetConnection(): Boolean {
    val connectivityManager = FieldBuzz.mContext.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.activeNetworkInfo?.run {
            return when (type) {
                ConnectivityManager.TYPE_WIFI -> true
                ConnectivityManager.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_ETHERNET -> true
                else -> false
            }
        }
    }
    return false
}