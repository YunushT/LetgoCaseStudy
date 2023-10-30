package com.yunushan.letgocasestudy.data.model.response

import com.google.gson.annotations.SerializedName

data class VehicleColorsResponse(
    val id: Int?,
    val name: String?,
    @SerializedName("code")
    val colorCode: String?
)
