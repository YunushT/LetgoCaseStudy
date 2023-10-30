package com.yunushan.letgocasestudy.data.model.response

import com.google.gson.annotations.SerializedName

data class VehiclePricingResponse(
    val id: Int?,
    val price: Int?,
    val code: String?,
    val transmission: VehicleTransmissionResponse?,
    val engine: VehicleEngineResponse?,
    @SerializedName("random_id")
    val randomId: String?
)