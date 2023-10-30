package com.yunushan.letgocasestudy.domain.usecase

import com.yunushan.letgocasestudy.data.model.response.VehicleSeriesItemResponse
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleSeriesItemUIModel
import com.yunushan.letgocasestudy.data.repository.Repository
import com.yunushan.letgocasestudy.domain.util.NetworkError
import com.yunushan.letgocasestudy.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class GetVehicleModelsUseCase @Inject constructor(
    private val repository: Repository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getVehicleModels(
        vehicleModel: String,
        vehicleBrandId: String,
        onSuccess: (List<VehicleSeriesItemUIModel>) -> Unit,
        onFailure: (Resource.Failure) -> Unit
    ) {
        return withContext(dispatcher) {
            repository.getVehicleByModelAndMakesId(vehicleModel, vehicleBrandId)
                .enqueue(object : Callback<List<VehicleSeriesItemResponse>> {

                    override fun onFailure(
                        call: Call<List<VehicleSeriesItemResponse>>,
                        t: Throwable
                    ) {
                        onFailure.invoke(
                            Resource.Failure(
                                NetworkError(
                                    400,
                                    "Unknown Network Error"
                                )
                            )
                        )
                    }

                    override fun onResponse(
                        call: Call<List<VehicleSeriesItemResponse>>,
                        response: Response<List<VehicleSeriesItemResponse>>
                    ) {
                        if (response.isSuccessful) {
                            Resource.Success(response.body()).data?.let {
                                onSuccess.invoke(it.map { item ->
                                    VehicleSeriesItemUIModel(
                                        item.id,
                                        item.name,
                                        item.code
                                    )
                                })
                            }

                        } else {
                            onFailure.invoke(
                                Resource.Failure(
                                    NetworkError(
                                        response.code(),
                                        response.message()
                                    )
                                )
                            )
                        }
                    }
                })
        }
    }
}