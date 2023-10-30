package com.yunushan.letgocasestudy.data.repository

import com.yunushan.letgocasestudy.data.model.request.VehiclePricingBody
import com.yunushan.letgocasestudy.data.model.response.VehicleBrandsListResponse
import com.yunushan.letgocasestudy.data.model.response.VehicleColorsResponse
import com.yunushan.letgocasestudy.data.model.response.VehicleItemDetailTrimsResponse
import com.yunushan.letgocasestudy.data.model.response.VehiclePricingResponse
import com.yunushan.letgocasestudy.data.model.response.VehicleSeriesItemResponse
import com.yunushan.letgocasestudy.data.repository.datasource.remote.RemoteDataSource
import retrofit2.Call
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : Repository {
    override suspend fun getPricingList(vehiclePricingBody: VehiclePricingBody): Call<VehiclePricingResponse> {
        return remoteDataSource.getPricingList(vehiclePricingBody)
    }

    override suspend fun getVehicleColors(): Call<List<VehicleColorsResponse>> {
        return remoteDataSource.getVehicleColors()
    }

    override suspend fun getVehicleModels(): Call<List<Int>> {
        return remoteDataSource.getVehicleModels()
    }

    override suspend fun getVehiclesBrandList(modelName: String): Call<List<VehicleBrandsListResponse>> {
        return remoteDataSource.getVehiclesBrandList(modelName)
    }

    override suspend fun getVehicleByModelAndMakesId(
        modelName: String,
        makesId: String
    ): Call<List<VehicleSeriesItemResponse>> {
        return remoteDataSource.getVehicleByModelAndMakesId(modelName, makesId)
    }

    override suspend fun getVehicleByModelMakesAndTrim(
        modelName: String,
        makesId: String,
        series: String
    ): Call<List<VehicleItemDetailTrimsResponse>> {
        return remoteDataSource.getVehicleByModelMakesAndTrim(modelName, makesId, series)
    }


}