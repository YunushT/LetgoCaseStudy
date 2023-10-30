package com.yunushan.letgocasestudy.presentation.event

import com.yunushan.letgocasestudy.data.model.uimodel.VehicleInfo
import com.yunushan.letgocasestudy.data.model.uimodel.VehiclePricingUIModel
import com.yunushan.letgocasestudy.domain.util.NetworkError

sealed class PriceResultEvent {
    object Idle : PriceResultEvent()
    data class PricingResult(
        val priceUIDetail: VehiclePricingUIModel,
        val vehicleInfo: VehicleInfo
    ) : PriceResultEvent()
    data class Error(val error: NetworkError) : PriceResultEvent()
}

