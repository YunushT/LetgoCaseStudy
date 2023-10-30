package com.yunushan.letgocasestudy.data.model.uimodel

data class VehicleItemDetailTrimsUIModel(
    val id: Int?,
    val name: String?,
    val code: String?,
    val displacement: String?,
    val bodyConfig: VehicleBodyConfigUIModel?,
    val transmission: VehicleTransmissionUIModel?,
    val engine: VehicleEngineUIModel?,

    )

data class VehicleBodyConfigUIModel(
    val id: Int?,
    val name: String?
)

data class VehicleTransmissionUIModel(
    val id: Int?,
    val name: String?,
)

data class VehicleEngineUIModel(
    val id: Int?,
    val name: String?
)
