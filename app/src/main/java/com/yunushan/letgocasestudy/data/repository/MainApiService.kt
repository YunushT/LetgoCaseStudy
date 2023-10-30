package com.yunushan.letgocasestudy.data.repository

import com.yunushan.letgocasestudy.data.model.request.VehiclePricingBody
import com.yunushan.letgocasestudy.data.model.response.VehicleBrandsListResponse
import com.yunushan.letgocasestudy.data.model.response.VehicleColorsResponse
import com.yunushan.letgocasestudy.data.model.response.VehicleItemDetailTrimsResponse
import com.yunushan.letgocasestudy.data.model.response.VehiclePricingResponse
import com.yunushan.letgocasestudy.data.model.response.VehicleSeriesItemResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface MainApiService {
    @POST("/public/api/pricings")
    fun getPricingList(
        @Body vehiclePricingBody: VehiclePricingBody
    ): Call<VehiclePricingResponse>

    @GET("/public/api/vehicles/colors")
    fun getVehicleColors(): Call<List<VehicleColorsResponse>>

    @GET("/public/api/vehicles/models")
    fun getVehicleModels(): Call<List<Int>>

    @GET("/public/api/vehicles/models/{model}/makes")
    fun getVehiclesBrandList(@Path("model") modelName: String): Call<List<VehicleBrandsListResponse>>

    @GET("/public/api/vehicles/models/{model}/makes/{makesId}/series")
    fun getVehicleByModelAndMakesId(
        @Path("model") modelName: String,
        @Path("makesId") makesId: String
    ): Call<List<VehicleSeriesItemResponse>>

    @GET("/public/api/vehicles/models/{model}/makes/{makesId}/series/{series}/trims")
    fun getVehicleByModelMakesAndTrim(
        @Path("model") modelName: String,
        @Path("makesId") makesId: String,
        @Path("series") series: String
    ): Call<List<VehicleItemDetailTrimsResponse>>

}