package com.soumik.fieldbuzz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.soumik.fieldbuzz.R
import com.soumik.fieldbuzz.utils.lightStatusBar
import com.soumik.fieldbuzz.utils.toolbarStyle

class HomeActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar(this,true)
        setContentView(R.layout.activity_home)

        init()

        toolbarStyle(this,toolbar,"Home")
    }

    private fun init() {
        toolbar = findViewById(R.id.tb_home)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        finish()
        return super.onOptionsItemSelected(item)
    }
}