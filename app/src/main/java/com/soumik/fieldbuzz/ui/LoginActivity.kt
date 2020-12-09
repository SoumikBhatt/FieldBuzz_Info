package com.soumik.fieldbuzz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.soumik.fieldbuzz.R
import com.soumik.fieldbuzz.utils.Status
import com.soumik.fieldbuzz.utils.lightStatusBar
import com.soumik.fieldbuzz.utils.showToast
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LOGIN"
    }

    private lateinit var mViewModel: LoginViewModel

    private lateinit var loginBtn:Button
    private lateinit var userNameET:EditText
    private lateinit var passwordET:EditText
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar(this,true)
        setContentView(R.layout.activity_login)

        init()

        loginBtn.setOnClickListener { validateInputFields() }

        setUpObservers()

    }

    private fun validateInputFields() {
        when {
            TextUtils.isEmpty(userNameET.text) -> userNameET.error = "Please provide your username"
            TextUtils.isEmpty(passwordET.text) -> passwordET.error = "Please provide your password"
            else -> lifecycleScope.launch { login(userNameET.text.toString(),passwordET.text.toString()) }
        }
    }

    private suspend fun login(userName: String, password: String) {

        mViewModel.login(userName,password)


    }

    private fun setUpObservers() {
        mViewModel.loginLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility=View.GONE
                    loginBtn.visibility=View.VISIBLE
                    showToast(this,"Logged in successfully!!",Toast.LENGTH_SHORT)
                }
                Status.ERROR -> {
                    progressBar.visibility=View.GONE
                    loginBtn.visibility=View.VISIBLE

//                    if (it.error=="Wrong Credentials!") {
//                        userNameET.error = ""
//                        passwordET.error =""
//                    }

                    showToast(this,it.error!!,Toast.LENGTH_SHORT)
                }
                Status.LOADING -> {
                    progressBar.visibility=View.VISIBLE
                    loginBtn.visibility=View.GONE
                }
            }
        })
    }

    private fun init() {
        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginBtn = findViewById(R.id.btn_login)
        userNameET = findViewById(R.id.et_username)
        passwordET = findViewById(R.id.et_password)
        progressBar = findViewById(R.id.pb_login)
    }
}