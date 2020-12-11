package com.soumik.fieldbuzz.data.models
import com.google.gson.annotations.SerializedName


data class DetailsResponse(
    @SerializedName("applying_in")
    val applyingIn: String?,
    @SerializedName("cgpa")
    val cgpa: Double?,
    @SerializedName("current_work_place_name")
    val currentWorkPlaceName: String?,
    @SerializedName("cv_file")
    val cvFile: CvFile?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("expected_salary")
    val expectedSalary: Int?,
    @SerializedName("experience_in_months")
    val experienceInMonths: Int?,
    @SerializedName("field_buzz_reference")
    val fieldBuzzReference: String?,
    @SerializedName("full_address")
    val fullAddress: String?,
    @SerializedName("github_project_url")
    val githubProjectUrl: String?,
    @SerializedName("graduation_year")
    val graduationYear: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("name_of_university")
    val nameOfUniversity: String?,
    @SerializedName("on_spot_creation_time")
    val onSpotCreationTime: Int?,
    @SerializedName("on_spot_update_time")
    val onSpotUpdateTime: Int?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("tsync_id")
    val tsyncId: String?
)

data class CvFile(
    @SerializedName("id")
    val id: Int,
    @SerializedName("tsync_id")
    val tsyncId: String?
)