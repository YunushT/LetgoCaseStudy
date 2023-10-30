package com.yunushan.letgocasestudy.data.model.response

import com.google.gson.annotations.SerializedName

data class VehicleItemDetailTrimsResponse(
    val id: Int?,
    val name: String?,
    val code: String?,
    val displacement: String?,
    @SerializedName("body_config")
    val bodyConfig: VehicleBodyConfigResponse?,
    val transmission: VehicleTransmissionResponse?,
    val engine: VehicleEngineResponse?,

    )

data class VehicleBodyConfigResponse(
    val id: Int?,
    val name: String?
)

data class VehicleTransmissionResponse(
    val id: Int?,
    val name: String?,
)

data class VehicleEngineResponse(
    val id: Int?,
    val name: String?
)
