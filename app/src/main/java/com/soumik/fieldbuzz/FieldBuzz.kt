package com.soumik.fieldbuzz

import android.app.Application
import android.content.Context

class FieldBuzz:Application() {

    companion object {
        lateinit var mContext:Context
    }

    override fun onCreate() {
        super.onCreate()

        mContext=applicationContext
    }
}