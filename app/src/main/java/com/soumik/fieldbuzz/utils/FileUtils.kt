package com.soumik.fieldbuzz.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.soumik.fieldbuzz.ui.HomeActivity
import java.io.File


object FileUtils {

    const val TAG = "FILE_UTIL"

    fun getFullPathFromContentUri(context: Context, uri: Uri?): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        if (uri!=null) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if ("com.android.externalstorage.documents" == uri.authority) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if ("com.android.providers.downloads.documents" == uri.authority) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                    )
                    return getDataColumn(context, contentUri, null, null)
                } else if ("com.android.providers.media.documents" == uri.getAuthority()) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    when (type) {
                        "image" -> {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        "video" -> {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        "audio" -> {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                        split[1]
                    )
                    var cursor: Cursor? = null
                    val column = "_data"
                    val projection = arrayOf(
                        column
                    )
                    try {
                        cursor = context.contentResolver.query(
                            uri, projection, selection, selectionArgs,
                            null
                        )
                        if (cursor != null && cursor.moveToFirst()) {
                            val column_index: Int = cursor.getColumnIndexOrThrow(column)
                            return cursor.getString(column_index)
                        }
                    } finally {
                        cursor?.close()
                    }
                    return null
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
        }

        // DocumentProvider

        return null
    }

    fun getPdfFileName(context: Context,selectedPDF: Uri?,parentView:View):String {
        var displayName: String? = null

        if (selectedPDF != null) {
            val uri = selectedPDF.toString()
            val file = File(uri)
            val path = file.absolutePath

            Log.d(TAG, "getPdfFileName: PATH: $path")

            if (uri.startsWith("content://")) {
                var cursor: Cursor? = null
                try {
                    cursor = context.contentResolver.query(selectedPDF, null, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
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

    fun getPdfFileSize(context: Context,selectedPDF: Uri?,parentView:View):Double? {
        var pdfSize: Double? = null

        if (selectedPDF != null) {
            val uri = selectedPDF.toString()
            val file = File(uri)
            val path = file.absolutePath

            Log.d(TAG, "getPdfFileName: PATH: $path")

//            if (uri.startsWith("content://")) {
                var cursor: Cursor? = null
                try {
                    cursor = context.contentResolver.query(selectedPDF, null, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        pdfSize = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)).toDouble() / 1000000

                        Log.d(TAG, "getPdfFileName: PDF Size: $pdfSize")
                    }
                } finally {
                    cursor?.close()
                }
//            } else if (uri.startsWith("file://")) {
//                pdfSize = file.
//            }
        } else {
            showSnackBar(parentView, "No pdf selected", "Ok", Snackbar.LENGTH_INDEFINITE)
            pdfSize = null
        }

        return pdfSize
    }

    fun getNameFromContentUri(context: Context, contentUri: Uri?,parentView: View): String? {
        var displayName: String? = null
        var returnCursor: Cursor?=null
        if (contentUri!=null) {
            try {
                returnCursor = context.contentResolver.query(contentUri, null, null, null, null)
                val nameColumnIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor?.moveToFirst()
                displayName = returnCursor?.getString(nameColumnIndex!!)
            } finally {
                returnCursor?.close()
            }
        } else {
            showSnackBar(parentView, "No pdf selected", "Ok", Snackbar.LENGTH_INDEFINITE)
            displayName = "No PDF selected"
        }

        return displayName
    }

    private fun getDataColumn(
        context: Context, uri: Uri, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }
}