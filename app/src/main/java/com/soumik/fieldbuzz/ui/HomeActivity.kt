package com.soumik.fieldbuzz.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.soumik.fieldbuzz.R
import com.soumik.fieldbuzz.utils.*
import kotlinx.coroutines.launch
import java.io.File


class HomeActivity : AppCompatActivity() {

    private lateinit var mViewModel: HomeViewModel

    private lateinit var toolbar: Toolbar
    private lateinit var nameInput:TextInputLayout
    private lateinit var emailInput:TextInputLayout
    private lateinit var phoneInput:TextInputLayout
    private lateinit var addressInput:TextInputLayout
    private lateinit var universityInput:TextInputLayout
    private lateinit var gradYearInput:TextInputLayout
    private lateinit var cGPAInput:TextInputLayout
    private lateinit var experienceInput:TextInputLayout
    private lateinit var workplaceInput:TextInputLayout
    private lateinit var salaryInput:TextInputLayout
    private lateinit var referenceInput:TextInputLayout
    private lateinit var urlInput:TextInputLayout
    private lateinit var applyingIn:RadioGroup
    private lateinit var pdfInput:RelativeLayout
    private lateinit var submitBtn:Button
    private lateinit var parentView:ConstraintLayout
    private lateinit var scrollView:ScrollView
    private lateinit var attachIV:ImageView
    private lateinit var pdfTV:TextView

    private lateinit var progressDialog:ProgressDialog

    private var applyingOn = "Mobile"
    private var selectedPDF:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar(this,true)
        setContentView(R.layout.activity_home)

        init()

        toolbarStyle(this,toolbar,"Home")
    }

    override fun onStart() {
        super.onStart()

        submitBtn.setOnClickListener {
            validateInputFields()
        }

        nameInput.editText?.doOnTextChanged { _, _, _, _ -> nameInput.error=null }
        emailInput.editText?.doOnTextChanged { _, _, _, _ -> emailInput.error=null }
        phoneInput.editText?.doOnTextChanged { _, _, _, _ -> phoneInput.error=null }
        addressInput.editText?.doOnTextChanged { _, _, _, _ -> addressInput.error=null }
        universityInput.editText?.doOnTextChanged { _, _, _, _ -> universityInput.error=null }
        gradYearInput.editText?.doOnTextChanged { _, _, _, _ -> gradYearInput.error=null }
        cGPAInput.editText?.doOnTextChanged { text, start, before, count ->
            cGPAInput.error=null
            if (!text.isNullOrBlank()) if (text.toString().toDouble()>4.0 || text.toString().toDouble()<2.0) cGPAInput.error = "cgpa must be in between 2.0-4.0"
        }
        experienceInput.editText?.doOnTextChanged { text, _, _, _ ->
            experienceInput.error=null

            if (!text.isNullOrBlank()) if (text.toString().toInt()>100 || text.toString().toInt()<0) experienceInput.error = "Experience can't be more than 100 months"
        }
        workplaceInput.editText?.doOnTextChanged { _, _, _, _ -> workplaceInput.error=null }
        salaryInput.editText?.doOnTextChanged { text, _, _, _ ->
            salaryInput.error=null

            if (!text.isNullOrBlank()) if (text.toString().toInt()>60000 || text.toString().toInt()<15000) salaryInput.error = "Salary expectation must be in between 15000-60000"
        }
        referenceInput.editText?.doOnTextChanged { _, _, _, _ -> referenceInput.error=null }
        urlInput.editText?.doOnTextChanged { _, _, _, _ -> urlInput.error=null }

        applyingIn.setOnCheckedChangeListener { _, checkedId ->
            applyingOn = findViewById<RadioButton>(checkedId).text.toString()
        }

        pdfInput.setOnClickListener { selectPdfFromStorage() }

        Log.d(TAG, "onStart: Selected: $applyingOn")

        setUpObserver()
    }

    private fun setUpObserver() {
        mViewModel.liveData.observe(this, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    progressDialog.hideProgressBar()
                    showSnackBar(parentView,"Information Submitted Successfully","Ok",Snackbar.LENGTH_INDEFINITE)
                }
                Status.ERROR-> {
                    progressDialog.hideProgressBar()
                    showSnackBar(parentView,it.error!!,"Ok",Snackbar.LENGTH_INDEFINITE)
                }
                Status.LOADING->{
                    progressDialog.message(it.error)
                }
            }
        })
    }

    private fun validateInputFields() {

        Log.d(TAG, "validateInputFields: Selected: $applyingOn")

        when {
            TextUtils.isEmpty(nameInput.editText?.text) -> {
                nameInput.error = "Name field is mandatory"
                scrollView.focusOnView(nameInput)
            }
            TextUtils.isEmpty(emailInput.editText?.text) -> {
                emailInput.error = "Email field is mandatory"
                scrollView.focusOnView(emailInput)
            }
            !emailInput.editText?.text.toString().isValidEmail() -> {
                emailInput.error = "Please provide a valid email address"
                scrollView.focusOnView(emailInput)
            }
            TextUtils.isEmpty(phoneInput.editText?.text) -> {
                phoneInput.error = "Phone field is mandatory"
                scrollView.focusOnView(phoneInput)
            }
            TextUtils.isEmpty(universityInput.editText?.text) -> {
                universityInput.error = "University field is mandatory"
                scrollView.focusOnView(universityInput)
            }
            TextUtils.isEmpty(gradYearInput.editText?.text) -> {
                gradYearInput.error = "Graduation year field is mandatory"
                scrollView.focusOnView(gradYearInput)
            }
            gradYearInput.editText?.text.toString().toInt()>2020 || gradYearInput.editText?.text.toString().toInt()<2015 -> {
                gradYearInput.error = "Graduation year must be in between 2015-2020"
                scrollView.focusOnView(gradYearInput)
            }
            TextUtils.isEmpty(salaryInput.editText?.text) -> {
                salaryInput.error = "Expected salary field is mandatory"
                scrollView.focusOnView(salaryInput)
            }
            salaryInput.editText?.text.toString().toInt()>60000 || salaryInput.editText?.text.toString().toInt()<15000 -> {
                salaryInput.error = "Salary expectation must be in between 15000-60000"
                scrollView.focusOnView(salaryInput)
            }
            TextUtils.isEmpty(urlInput.editText?.text) -> {
                urlInput.error = "Project url field is mandatory"
                scrollView.focusOnView(urlInput)
            }
            !urlInput.editText?.text.toString().isValidUrl() -> {
                urlInput.error = "Please provide a valid url"
                scrollView.focusOnView(urlInput)
            }
            cGPAInput.editText?.text.toString().isNotBlank() && (cGPAInput.editText?.text.toString().toDouble()>4.0 || cGPAInput.editText?.text.toString().toDouble()<2.0)-> {
                    cGPAInput.error = "cgpa must be in between 2.0-4.0"
                    showSnackBar(parentView,"cgpa must be in between 2.0-4.0","Ok",Snackbar.LENGTH_INDEFINITE)
                    scrollView.focusOnView(cGPAInput)
                }
            experienceInput.editText?.text.toString().isNotBlank() && (experienceInput.editText?.text.toString().toInt()>100 || experienceInput.editText?.text.toString().toInt()<0) -> {
                    experienceInput.error = "Experience can't be more than 100 months"
                    showSnackBar(parentView,"Experience can't be more than 100 months","Ok",Snackbar.LENGTH_INDEFINITE)
                    scrollView.focusOnView(experienceInput)
            }
            selectedPDF==null -> showSnackBar(parentView,"Please choose your cv","Ok",Snackbar.LENGTH_INDEFINITE)
            else -> {
                val inputToken = getRandomInputToken()
                val fileToken = getRandomFileToken()

                Log.d(TAG, "validateInputFields: IT: $inputToken :: FT: $fileToken")

                submitInformation(nameInput.editText?.text.toString(),emailInput.editText?.text.toString(),phoneInput.editText?.text.toString(),addressInput.editText?.text.toString(),
                universityInput.editText?.text.toString(),gradYearInput.editText?.text.toString().toInt(),cGPAInput.editText?.text.toString().toDouble(),experienceInput.editText?.text.toString().toInt(),
                workplaceInput.editText?.text.toString(),applyingOn,salaryInput.editText?.text.toString().toInt(),referenceInput.editText?.text.toString(),urlInput.editText?.text.toString(),
                selectedPDF,inputToken,fileToken, unixTimestamp, unixTimestamp)
            }
        }
    }

    private fun submitInformation(
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
    ) {

        Log.d(TAG, "submitInformation: N: $name :: E: $email :: P: $phone :: A: $address :: U: $university :: " +
                "GY: $gradYear :: C: $cgpa :: E: $experience :: W: $workPlace :: APP: $applyingOn :: S: $salary :: REF: $reference ::" +
                " PU: $projectUrl :: IT: $inputToken :: FT: $fileToken :: UT: $updateTime :: CT: $createTime")
//        progressDialog(this,"Submitting Information...").show()


        progressDialog.showProgress(this,null)

        lifecycleScope.launch {
            mViewModel.submitInformation(name, email, phone, address, university, gradYear, cgpa,
                experience, workPlace, applyingOn, salary, reference, projectUrl, selectedPDF, inputToken, fileToken, updateTime, createTime)
        }

    }

    private fun init() {

        mViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        progressDialog = ProgressDialog(this)

        toolbar = findViewById(R.id.tb_home)
        nameInput = findViewById(R.id.con_name)
        emailInput = findViewById(R.id.con_email)
        phoneInput = findViewById(R.id.con_phone)
        addressInput = findViewById(R.id.con_address)
        universityInput = findViewById(R.id.con_university)
        gradYearInput = findViewById(R.id.con_grad_year)
        cGPAInput = findViewById(R.id.con_gpa)
        experienceInput = findViewById(R.id.con_experience)
        workplaceInput = findViewById(R.id.con_workplace)
        salaryInput = findViewById(R.id.con_salary)
        referenceInput = findViewById(R.id.con_reference)
        urlInput = findViewById(R.id.con_project)
        applyingIn = findViewById(R.id.rg_applying_in)
        pdfInput = findViewById(R.id.rl_pdf)
        submitBtn = findViewById(R.id.btn_submit)
        parentView = findViewById(R.id.cl_parent)
        scrollView = findViewById(R.id.scrollView)
        attachIV = findViewById(R.id.iv_attach)
        pdfTV= findViewById(R.id.tv_attach_pic)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedPDF = data.data

            attachIV.setImageResource(R.drawable.ic_pdf)
            pdfTV.text = getPdfFileName(selectedPDF)
        }
    }

    private fun getPdfFileName(selectedPDF: Uri?):String {
        var displayName: String? = null

        if (selectedPDF != null) {
            val uri = selectedPDF.toString()
            val file = File(uri)
            val path = file.absolutePath

            if (uri.startsWith("content://")) {
                var cursor: Cursor? = null
                try {
                    cursor = contentResolver.query(selectedPDF, null, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                } finally {
                    cursor?.close()
                }
            } else if (uri.startsWith("file://")) {
                displayName = file.name
            }
        } else {
            showSnackBar(parentView, "No pdf selected", "Ok", Snackbar.LENGTH_INDEFINITE)
            displayName = "No PDF selected"
        }

        return displayName!!
    }

    private fun selectPdfFromStorage() {
        val browseStorage = Intent(Intent.ACTION_GET_CONTENT)
        browseStorage.type = "application/pdf"
        browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(browseStorage, "Select PDF"), PDF_SELECTION_CODE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        finish()
        return super.onOptionsItemSelected(item)
    }

    companion object{
        private const val TAG = "HOME"
        private const val PDF_SELECTION_CODE = 1234
    }
}