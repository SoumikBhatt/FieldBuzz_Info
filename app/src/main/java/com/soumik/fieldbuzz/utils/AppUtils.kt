package com.soumik.fieldbuzz.utils

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.soumik.fieldbuzz.FieldBuzz
import com.soumik.fieldbuzz.R

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


fun getRandomInputToken() : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    val token =  (1..55).map { allowedChars.random() }.joinToString("")

    return  if (token!=SessionManager.lastInputToken) {
        SessionManager.lastInputToken=token
        token
    } else getRandomInputToken()
}

fun getRandomFileToken() : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    val token =  (1..55).map { allowedChars.random() }.joinToString("")

    return  if (token!=SessionManager.lastFileToken) {
        SessionManager.lastFileToken=token
        token
    } else getRandomFileToken()
}

val unixTimestamp =  System.currentTimeMillis()

fun String.isValidEmail():Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()

fun progressDialog(context: Context,message: String?): Dialog {
    val dialog = Dialog(context)
    val view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)

    val messageTV = view.findViewById<TextView>(R.id.tv_loading_text)

    if (message!=null) messageTV.text = message

    dialog.setContentView(view)
    dialog.setCancelable(false)
    dialog.window!!.setBackgroundDrawable(
        ColorDrawable(Color.TRANSPARENT)
    )
    return dialog
}

//private val changeText = Runnable { m_ProgressDialog.setMessage(myText) }

fun ScrollView.focusOnView(toView: View){

    Handler(Looper.myLooper()!!).post {
        this.smoothScrollTo(0, toView.top)
    }
}


fun ProgressDialog.showProgress(activity: Activity,message: String?) {
    val progressDialog = this
    progressDialog.isIndeterminate = true
    progressDialog.setMessage(message)
    progressDialog.setCancelable(true)
    progressDialog.show()
}

fun ProgressDialog.hideProgressBar() {
    this.dismiss()
}

fun ProgressDialog.message(message: String?) {
    this.setMessage(message)
}

