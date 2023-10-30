package com.yunushan.letgocasestudy.data.model.uimodel

data class VehiclePricingUIModel(
    val id: Int?,
    val price: Int?,
    val code: String?,
    val transmission: VehicleTransmissionUIModel?,
    val engine: VehicleEngineUIModel?,
    val randomId: String?,
    var pageTitle: Int? = null
)