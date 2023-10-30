package com.yunushan.letgocasestudy.data.repository

import com.yunushan.letgocasestudy.data.model.request.VehiclePricingBody
import com.yunushan.letgocasestudy.data.model.response.VehicleBrandsListResponse
import com.yunushan.letgocasestudy.data.model.response.VehicleColorsResponse
import com.yunushan.letgocasestudy.data.model.response.VehicleItemDetailTrimsResponse
import com.yunushan.letgocasestudy.data.model.response.VehiclePricingResponse
import com.yunushan.letgocasestudy.data.model.response.VehicleSeriesItemResponse
import retrofit2.Call

interface Repository {
    suspend fun getPricingList(vehiclePricingBody: VehiclePricingBody): Call<VehiclePricingResponse>
    suspend fun getVehicleColors(): Call<List<VehicleColorsResponse>>
    suspend fun getVehicleModels(): Call<List<Int>>
    suspend fun getVehiclesBrandList(modelName: String): Call<List<VehicleBrandsListResponse>>
    suspend fun getVehicleByModelAndMakesId(
        modelName: String,
        makesId: String
    ): Call<List<VehicleSeriesItemResponse>>

    suspend fun getVehicleByModelMakesAndTrim(
        modelName: String,
        makesId: String,
        series: String
    ): Call<List<VehicleItemDetailTrimsResponse>>
}