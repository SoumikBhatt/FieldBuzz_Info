package com.soumik.fieldbuzz.data.models
import com.google.gson.annotations.SerializedName


data class FileUploadResponse(
    @SerializedName("code")
    val code: String?,
    @SerializedName("date_created")
    val dateCreated: Long?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("extension")
    val extension: String?,
    @SerializedName("file")
    val `file`: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("last_updated")
    val lastUpdated: Long?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("tsync_id")
    val tsyncId: String?
)