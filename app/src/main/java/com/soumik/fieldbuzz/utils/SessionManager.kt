package com.soumik.fieldbuzz.utils

import android.content.Context
import com.soumik.fieldbuzz.FieldBuzz

object SessionManager {

    private const val PREFERENCE_NAME = "fieldBuzzSessions"

    private const val IS_LOGGED_IN = "loggedIn"
    private const val TOKEN = "token"
    private const val LOGIN_TOKEN = "login_token"
    private const val LAST_INPUT_TOKEN = "last_input_token"
    private const val FILE_TOKEN = "file_token"
    private const val LAST_FILE_TOKEN = "last_file_token"

    private val mContext = FieldBuzz.mContext

    private var preference = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private var editor = preference.edit()

    var isLoggedIn: Boolean
    get() = preference.getBoolean(IS_LOGGED_IN, false)
    set(value) { editor.putBoolean(IS_LOGGED_IN, value).commit() }

    var token:String?
    get() = preference.getString(TOKEN,null)
    set(value) { editor.putString(TOKEN,value).commit()}

    var lastInputToken:String?
    get() = preference.getString(LOGIN_TOKEN,null)
    set(value) {
        editor.putString(LOGIN_TOKEN,value).commit()}

    var lastFileToken:String?
        get() = preference.getString(FILE_TOKEN,null)
        set(value) {
            editor.putString(FILE_TOKEN,value).commit()}

    var lastSavedInputToken:String?
    get() = preference.getString(LAST_INPUT_TOKEN,null)
    set(value) {
        editor.putString(LAST_INPUT_TOKEN,value).commit()}

    var lastSavedFileToken:String?
        get() = preference.getString(LAST_FILE_TOKEN,null)
        set(value) {
            editor.putString(LAST_FILE_TOKEN,value).commit()}
}