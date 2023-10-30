package com.yunushan.letgocasestudy.data.repository.datasource.remote


import com.yunushan.letgocasestudy.data.model.request.VehiclePricingBody
import com.yunushan.letgocasestudy.data.model.response.VehicleBrandsListResponse
import com.yunushan.letgocasestudy.data.model.response.VehicleColorsResponse
import com.yunushan.letgocasestudy.data.model.response.VehicleItemDetailTrimsResponse
import com.yunushan.letgocasestudy.data.model.response.VehiclePricingResponse
import com.yunushan.letgocasestudy.data.model.response.VehicleSeriesItemResponse
import com.yunushan.letgocasestudy.data.repository.MainApiService
import retrofit2.Call
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val apiService: MainApiService) :
    RemoteDataSource {
    override suspend fun getPricingList(vehiclePricingBody: VehiclePricingBody): Call<VehiclePricingResponse> {
        return apiService.getPricingList(vehiclePricingBody)
    }

    override suspend fun getVehicleColors(): Call<List<VehicleColorsResponse>> {
        return apiService.getVehicleColors()
    }

    override suspend fun getVehicleModels(): Call<List<Int>> {
        return apiService.getVehicleModels()
    }

    override suspend fun getVehiclesBrandList(modelName: String): Call<List<VehicleBrandsListResponse>> {
        return apiService.getVehiclesBrandList(modelName)
    }

    override suspend fun getVehicleByModelAndMakesId(
        modelName: String,
        makesId: String
    ): Call<List<VehicleSeriesItemResponse>> {
        return apiService.getVehicleByModelAndMakesId(modelName, makesId)
    }

    override suspend fun getVehicleByModelMakesAndTrim(
        modelName: String, makesId: String,
        series: String
    ): Call<List<VehicleItemDetailTrimsResponse>> {
        return apiService.getVehicleByModelMakesAndTrim(modelName, makesId, series)
    }
}